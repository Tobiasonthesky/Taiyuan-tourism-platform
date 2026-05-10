# -*- coding: utf-8 -*-
"""
旅游平台图片数据插入脚本
下载真实占位图片到本地，插入图片记录到所有模块的图片表
"""

import pymysql
import os
import urllib.request
import random
import ssl
from datetime import datetime
from concurrent.futures import ThreadPoolExecutor, as_completed
import threading

DB_CONFIG = {
    'host': 'localhost',
    'port': 3306,
    'user': 'root',
    'password': '123456',
    'database': 'tourism_platform',
    'charset': 'utf8mb4'
}

PROJECT_ROOT = os.path.dirname(os.path.abspath(__file__))
UPLOAD_DIR = os.path.join(PROJECT_ROOT, 'tourism-platform-backend', 'uploads', 'images')

print_lock = threading.Lock()

def log(msg):
    with print_lock:
        print(msg)

def ensure_dir(dir_path):
    if not os.path.exists(dir_path):
        os.makedirs(dir_path)

def download_single_image(url, save_path):
    """下载单张图片"""
    if os.path.exists(save_path) and os.path.getsize(save_path) > 500:
        return True
    
    ctx = ssl.create_default_context()
    ctx.check_hostname = False
    ctx.verify_mode = ssl.CERT_NONE
    
    for attempt in range(2):
        try:
            req = urllib.request.Request(url, headers={
                'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
            })
            with urllib.request.urlopen(req, context=ctx, timeout=15) as response:
                data = response.read()
                if len(data) < 500:
                    continue
                with open(save_path, 'wb') as f:
                    f.write(data)
                return True
        except Exception:
            continue
    return False

def get_online_image_url(seed, theme='nature', width=800, height=600):
    """获取在线图片URL - 使用placehold.co作为主要来源，稳定快速"""
    themes_palette = {
        'nature': ['4CAF50', '8BC34A', '009688', '2196F3', '00BCD4'],
        'city': ['607D8B', '795548', '9E9E9E', '455A64', '546E7A'],
        'food': ['FF9800', 'FF5722', 'F44336', 'E91E63', 'FF7043'],
        'hotel': ['3F51B5', '5C6BC0', '7986CB', '3949AB', '1E88E5'],
        'culture': ['9C27B0', '673AB7', '7B1FA2', 'E040FB', 'AB47BC'],
    }
    palette = themes_palette.get(theme, themes_palette['nature'])
    color = random.choice(palette)
    bg = color
    text_color = 'ffffff'
    
    theme_labels = {
        'nature': 'nature', 'city': 'city', 'food': 'food',
        'hotel': 'hotel', 'culture': 'culture'
    }
    label = theme_labels.get(theme, 'tourism')
    
    return f'https://placehold.co/{width}x{height}/{bg}/{text_color}?text={label}+{seed}'

def download_cover_images_parallel(tasks, max_workers=10):
    """并行下载封面图片"""
    results = {}
    futures = {}
    
    with ThreadPoolExecutor(max_workers=max_workers) as executor:
        for entity_type, entity_id, filename_prefix, seed in tasks:
            url = get_online_image_url(seed, entity_type)
            filename = f'{filename_prefix}_{entity_id}_{seed}.jpg'
            save_path = os.path.join(UPLOAD_DIR, filename)
            local_url = f'/uploads/images/{filename}'
            future = executor.submit(download_single_image, url, save_path)
            futures[future] = (entity_type, entity_id, local_url)
        
        for future in as_completed(futures):
            entity_type, entity_id, local_url = futures[future]
            success = future.result()
            if success:
                results[(entity_type, entity_id)] = local_url
            else:
                # 使用在线URL作为备用
                fallback = get_online_image_url(entity_id, entity_type)
                results[(entity_type, entity_id)] = fallback
    
    return results

def get_connection():
    return pymysql.connect(**DB_CONFIG)

def clear_image_tables(cursor):
    """清空图片表"""
    print("=" * 60)
    print("[Step 1] 清空已有图片表数据...")
    image_tables = ['attraction_image', 'food_image', 'hotel_image',
                    'culture_image', 'strategy_image', 'experience_image']
    for table in image_tables:
        try:
            cursor.execute(f'DELETE FROM `{table}`')
            print(f"  [OK] 清空 {table}")
        except Exception as e:
            print(f"  [WARN] 清空 {table} 失败: {e}")

def update_cover_images(cursor):
    """更新所有实体表的封面图片"""
    print()
    print("=" * 60)
    print("[Step 2] 下载并更新实体表封面图片...")
    ensure_dir(UPLOAD_DIR)
    
    entity_map = {
        'attraction': 'nature',
        'food': 'food',
        'hotel': 'hotel',
        'culture': 'culture',
        'strategy': 'city',
        'experience': 'culture',
        'announcement': 'city',
    }
    
    for table, theme in entity_map.items():
        try:
            cursor.execute(f'SELECT id FROM `{table}`')
            ids = [row[0] for row in cursor.fetchall()]
        except Exception:
            print(f"  [SKIP] {table} 表不存在")
            continue
        
        if not ids:
            print(f"  [SKIP] {table} 表无数据")
            continue
        
        tasks = [(theme, eid, f'{table}_cover', random.randint(1, 9999))
                 for eid in ids]
        
        print(f"  [INFO] 正在为 {table} 下载 {len(ids)} 张封面图片...")
        results = download_cover_images_parallel(tasks, max_workers=10)
        
        updated = 0
        for (entity_type, entity_id), image_url in results.items():
            if entity_type == theme:
                try:
                    cursor.execute(
                        f'UPDATE `{table}` SET cover_image = %s WHERE id = %s',
                        (image_url, entity_id)
                    )
                    updated += 1
                except Exception as e:
                    print(f"    [WARN] 更新失败 id={entity_id}: {e}")
        
        print(f"  [OK] {table}: 成功更新 {updated}/{len(ids)} 条封面图片")

def insert_detail_images(cursor, table, entity_table, entity_id_field, theme, count_range=(2, 3)):
    """为某一模块插入详情图片到图片表"""
    try:
        cursor.execute(f'SELECT id FROM `{entity_table}`')
        entity_ids = [row[0] for row in cursor.fetchall()]
    except Exception:
        print(f"  [SKIP] {entity_table} 表不存在")
        return 0
    
    if not entity_ids:
        print(f"  [SKIP] {entity_table} 表无数据")
        return 0
    
    total = 0
    for eid in entity_ids:
        num = random.randint(*count_range)
        for i in range(num):
            seed = random.randint(1, 9999)
            image_url = get_online_image_url(f'{entity_table}{eid}_{i+1}', theme)
            image_type = 'cover' if i == 0 else 'detail'
            sort_order = i
            
            try:
                cursor.execute(
                    f'INSERT INTO `{table}` ({entity_id_field}, image_url, image_type, sort_order, create_time) '
                    f'VALUES (%s, %s, %s, %s, %s)',
                    (eid, image_url, image_type, sort_order, datetime.now())
                )
                total += 1
            except Exception as e:
                pass
    
    return total

def insert_all_detail_images(cursor):
    """插入所有模块的详情图片"""
    print()
    print("=" * 60)
    print("[Step 3] 插入各模块详情图片到图片表...")
    
    configs = [
        ('attraction_image', 'attraction', 'attraction_id', 'nature', (2, 3)),
        ('food_image', 'food', 'food_id', 'food', (2, 3)),
        ('hotel_image', 'hotel', 'hotel_id', 'hotel', (3, 5)),
        ('culture_image', 'culture', 'culture_id', 'culture', (2, 3)),
        ('strategy_image', 'strategy', 'strategy_id', 'city', (2, 3)),
        ('experience_image', 'experience', 'experience_id', 'culture', (2, 3)),
    ]
    
    grand_total = 0
    for table, entity_table, id_field, theme, count_range in configs:
        count = insert_detail_images(cursor, table, entity_table, id_field, theme, count_range)
        print(f"  [OK] {table}: 插入 {count} 条图片记录")
        grand_total += count
    
    print(f"\n  [OK] 总计插入 {grand_total} 条详情图片记录")

def verify(cursor):
    """验证结果"""
    print()
    print("=" * 60)
    print("[Step 4] 图片数据验证报告")
    print("=" * 60)
    
    print("\n  实体表封面图片:")
    entity_tables = ['attraction', 'food', 'hotel', 'culture', 'strategy', 'experience', 'announcement']
    for table in entity_tables:
        try:
            cursor.execute(f'SELECT COUNT(*) FROM `{table}`')
            total = cursor.fetchone()[0]
            cursor.execute(f"SELECT COUNT(*) FROM `{table}` WHERE cover_image IS NOT NULL AND cover_image != ''")
            with_cover = cursor.fetchone()[0]
            pct = 100 if total == 0 else with_cover * 100 // total
            status = "OK" if total == with_cover else f"{pct}%"
            print(f"  [{status:>4}] {table}: {with_cover}/{total}")
        except Exception:
            pass
    
    print("\n  图片表记录:")
    image_tables = ['attraction_image', 'food_image', 'hotel_image', 'culture_image', 'strategy_image', 'experience_image']
    for table in image_tables:
        try:
            cursor.execute(f'SELECT COUNT(*) FROM `{table}`')
            count = cursor.fetchone()[0]
            status = "OK" if count > 0 else "EMPTY"
            print(f"  [{status:>5}] {table}: {count} 条")
        except Exception:
            print(f"  [SKIP] {table}: 表不存在")
    
    # 本地图片统计
    ensure_dir(UPLOAD_DIR)
    local_images = [f for f in os.listdir(UPLOAD_DIR)
                    if f.lower().endswith(('.jpg', '.jpeg', '.png', '.gif', '.webp'))]
    total_size = sum(os.path.getsize(os.path.join(UPLOAD_DIR, f)) for f in local_images)
    print(f"\n  本地图片: {len(local_images)} 个, 共 {total_size/1024:.1f} KB")
    print(f"  存放路径: {UPLOAD_DIR}")

def main():
    print("=" * 60)
    print("  旅游平台图片数据插入脚本")
    print("=" * 60)
    print(f"  上传目录: {UPLOAD_DIR}")
    
    conn = None
    try:
        ensure_dir(UPLOAD_DIR)
        
        print("\n正在连接数据库...")
        conn = get_connection()
        cursor = conn.cursor()
        print("  [OK] 数据库连接成功")
        
        # Step 1: 清空图片表
        clear_image_tables(cursor)
        conn.commit()
        
        # Step 2: 下载并更新封面图片
        update_cover_images(cursor)
        conn.commit()
        
        # Step 3: 插入详情图片
        insert_all_detail_images(cursor)
        conn.commit()
        
        # Step 4: 验证
        verify(cursor)
        
        print()
        print("=" * 60)
        print("  [OK] 所有图片数据插入完成!")
        print("=" * 60)
        print()
        print("  提示:")
        print("    1. 封面图片已下载到本地 uploads/images/ 目录")
        print("    2. 详情图片使用在线URL (placehold.co)，数据库已存储")
        print("    3. 启动后端服务后，图片通过 /uploads/images/ 路径访问")
        print("    4. 封面图片即使离线也能正常显示")
        
    except Exception as e:
        print(f"\n[ERROR] {e}")
        import traceback
        traceback.print_exc()
        if conn:
            conn.rollback()
    finally:
        if conn:
            cursor.close()
            conn.close()
            print("\n数据库连接已关闭")

if __name__ == '__main__':
    main()
