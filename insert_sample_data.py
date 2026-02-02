#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
旅游平台示例数据插入脚本
插入50条真实的示例数据到数据库
"""

import pymysql
import random
import json
from datetime import datetime, timedelta

# 数据库配置（请根据实际情况修改）
DB_CONFIG = {
    'host': 'localhost',
    'port': 3306,
    'user': 'root',
    'password': '123456',
    'database': 'tourism_platform',
    'charset': 'utf8mb4'
}

# 太原市景点数据
ATTRACTIONS = [
    {"name": "晋祠", "address": "山西省太原市晋源区晋祠镇", "longitude": 112.485, "latitude": 37.707, 
     "description": "中国现存最早的皇家祭祀园林，晋国宗祠，有3000多年历史", 
     "opening_hours": "08:00-18:00", "ticket_price": 80, "tags": "历史,文化,古建筑"},
    {"name": "双塔寺", "address": "山西省太原市迎泽区双塔寺街", "longitude": 112.568, "latitude": 37.857,
     "description": "太原标志性建筑，明代古塔，登塔可俯瞰全城", 
     "opening_hours": "08:30-17:30", "ticket_price": 30, "tags": "古建筑,登高,历史"},
    {"name": "太原古县城", "address": "山西省太原市晋源区", "longitude": 112.478, "latitude": 37.715,
     "description": "明代古城，保存完好的古建筑群，体验古代市井文化", 
     "opening_hours": "09:00-21:00", "ticket_price": 50, "tags": "古城,文化,历史"},
    {"name": "蒙山大佛", "address": "山西省太原市晋源区", "longitude": 112.456, "latitude": 37.732,
     "description": "世界第二大石佛，开凿于北齐，高约66米", 
     "opening_hours": "08:00-18:00", "ticket_price": 70, "tags": "佛教,历史,自然"},
    {"name": "汾河公园", "address": "山西省太原市汾河两岸", "longitude": 112.549, "latitude": 37.870,
     "description": "城市生态公园，沿汾河而建，适合休闲散步", 
     "opening_hours": "全天开放", "ticket_price": 0, "tags": "公园,休闲,自然"},
    {"name": "山西博物院", "address": "山西省太原市万柏林区滨河西路", "longitude": 112.520, "latitude": 37.862,
     "description": "国家一级博物馆，展示山西历史文化", 
     "opening_hours": "09:00-17:00", "ticket_price": 0, "tags": "博物馆,文化,历史"},
    {"name": "迎泽公园", "address": "山西省太原市迎泽区迎泽大街", "longitude": 112.562, "latitude": 37.860,
     "description": "太原市中心最大的公园，有湖泊和古建筑", 
     "opening_hours": "06:00-22:00", "ticket_price": 0, "tags": "公园,休闲,自然"},
    {"name": "天龙山石窟", "address": "山西省太原市晋源区", "longitude": 112.432, "latitude": 37.748,
     "description": "北朝至唐代石窟艺术，佛教造像精美", 
     "opening_hours": "08:00-17:00", "ticket_price": 50, "tags": "石窟,佛教,历史"},
    {"name": "太山", "address": "山西省太原市晋源区", "longitude": 112.445, "latitude": 37.725,
     "description": "太原名山，有古寺和自然风光", 
     "opening_hours": "08:00-18:00", "ticket_price": 25, "tags": "山,自然,古寺"},
    {"name": "青龙古镇", "address": "山西省太原市阳曲县", "longitude": 112.678, "latitude": 38.058,
     "description": "明清古镇，保存完好的古建筑群", 
     "opening_hours": "09:00-18:00", "ticket_price": 60, "tags": "古镇,文化,历史"},
    {"name": "中国煤炭博物馆", "address": "山西省太原市万柏林区", "longitude": 112.515, "latitude": 37.855,
     "description": "全国唯一煤炭行业博物馆，了解煤炭文化", 
     "opening_hours": "09:00-17:00", "ticket_price": 60, "tags": "博物馆,工业,文化"},
    {"name": "东湖醋园", "address": "山西省太原市杏花岭区", "longitude": 112.578, "latitude": 37.888,
     "description": "老陈醋文化园，体验传统制醋工艺", 
     "opening_hours": "08:30-17:30", "ticket_price": 20, "tags": "文化,体验,传统"},
    {"name": "太原植物园", "address": "山西省太原市晋源区", "longitude": 112.485, "latitude": 37.695,
     "description": "现代化植物园，展示各种植物和花卉", 
     "opening_hours": "09:00-18:00", "ticket_price": 50, "tags": "植物园,自然,休闲"},
    {"name": "纯阳宫", "address": "山西省太原市迎泽区", "longitude": 112.565, "latitude": 37.863,
     "description": "道教宫观，元代建筑，有精美的道教造像", 
     "opening_hours": "08:30-17:30", "ticket_price": 30, "tags": "道教,古建筑,历史"},
    {"name": "崛围山", "address": "山西省太原市尖草坪区", "longitude": 112.512, "latitude": 37.945,
     "description": "太原名山，有古寺和红叶，秋季最美", 
     "opening_hours": "08:00-18:00", "ticket_price": 20, "tags": "山,自然,古寺"},
]

# 太原美食数据
FOODS = [
    {"name": "刀削面", "description": "山西传统面食，面条宽厚，口感劲道", 
     "ingredients": "面粉,水,猪肉,青菜", "origin": "山西传统面食，历史悠久"},
    {"name": "过油肉", "description": "山西名菜，肉片滑嫩，配菜丰富", 
     "ingredients": "猪肉,木耳,黄瓜,鸡蛋", "origin": "山西传统名菜"},
    {"name": "糖醋里脊", "description": "酸甜可口的经典菜品", 
     "ingredients": "猪里脊,糖,醋,番茄酱", "origin": "山西家常菜"},
    {"name": "老陈醋", "description": "山西特产，酸香醇厚，历史悠久", 
     "ingredients": "高粱,大麦,豌豆", "origin": "山西传统特产，有3000年历史"},
    {"name": "平遥牛肉", "description": "山西名产，肉质鲜嫩，香味浓郁", 
     "ingredients": "牛肉,盐,香料", "origin": "平遥传统名产"},
    {"name": "莜面栲栳栳", "description": "山西特色面食，形状独特，口感Q弹", 
     "ingredients": "莜面,水", "origin": "山西传统面食"},
    {"name": "头脑", "description": "太原特色早餐，营养丰富", 
     "ingredients": "羊肉,山药,黄酒,面粉", "origin": "太原传统名食"},
    {"name": "羊杂割", "description": "山西特色小吃，汤鲜味美", 
     "ingredients": "羊杂,羊汤,香菜", "origin": "山西传统小吃"},
    {"name": "灌肠", "description": "山西特色小吃，口感独特", 
     "ingredients": "荞面,水", "origin": "山西传统小吃"},
    {"name": "猫耳朵", "description": "山西传统面食，形状像猫耳朵", 
     "ingredients": "面粉,水,配菜", "origin": "山西传统面食"},
    {"name": "剔尖", "description": "山西特色面食，制作技艺独特", 
     "ingredients": "面粉,水", "origin": "山西传统面食"},
    {"name": "拨鱼儿", "description": "山西特色面食，形状像小鱼", 
     "ingredients": "面粉,水", "origin": "山西传统面食"},
    {"name": "碗托", "description": "山西特色小吃，口感爽滑", 
     "ingredients": "荞面,水", "origin": "山西传统小吃"},
    {"name": "油糕", "description": "山西传统糕点，外酥内软", 
     "ingredients": "黄米面,豆沙,糖", "origin": "山西传统糕点"},
    {"name": "太谷饼", "description": "山西名点，香甜可口", 
     "ingredients": "面粉,糖,芝麻", "origin": "太谷传统名点"},
]

# 酒店数据
HOTELS = [
    {"name": "太原迎泽宾馆", "address": "山西省太原市迎泽区迎泽大街189号", 
     "longitude": 112.562, "latitude": 37.860, "phone": "0351-8828888", "star_level": 5},
    {"name": "山西饭店", "address": "山西省太原市迎泽区纯阳宫街21号", 
     "longitude": 112.565, "latitude": 37.863, "phone": "0351-8228888", "star_level": 4},
    {"name": "太原万达文华酒店", "address": "山西省太原市杏花岭区解放路169号", 
     "longitude": 112.578, "latitude": 37.888, "phone": "0351-8888888", "star_level": 5},
    {"name": "太原凯宾斯基酒店", "address": "山西省太原市小店区长风街113号", 
     "longitude": 112.545, "latitude": 37.825, "phone": "0351-8999999", "star_level": 5},
    {"name": "太原万怡酒店", "address": "山西省太原市万柏林区长风西街1号", 
     "longitude": 112.520, "latitude": 37.862, "phone": "0351-8777777", "star_level": 4},
    {"name": "太原丽华大酒店", "address": "山西省太原市小店区长风街12号", 
     "longitude": 112.548, "latitude": 37.830, "phone": "0351-8666666", "star_level": 4},
    {"name": "太原晋祠宾馆", "address": "山西省太原市晋源区晋祠镇", 
     "longitude": 112.485, "latitude": 37.707, "phone": "0351-8555555", "star_level": 4},
    {"name": "太原如家酒店", "address": "山西省太原市迎泽区解放路88号", 
     "longitude": 112.570, "latitude": 37.870, "phone": "0351-8444444", "star_level": 3},
    {"name": "太原汉庭酒店", "address": "山西省太原市小店区平阳路99号", 
     "longitude": 112.550, "latitude": 37.840, "phone": "0351-8333333", "star_level": 3},
    {"name": "太原7天酒店", "address": "山西省太原市万柏林区漪汾街66号", 
     "longitude": 112.510, "latitude": 37.880, "phone": "0351-8222222", "star_level": 2},
]

# 房间类型
ROOM_TYPES = [
    {"room_type": "标准间", "room_name": "标准双床房", "area": 25, "bed_type": "双床", "max_occupancy": 2, "price": 200},
    {"room_type": "大床房", "room_name": "豪华大床房", "area": 30, "bed_type": "大床", "max_occupancy": 2, "price": 250},
    {"room_type": "套房", "room_name": "行政套房", "area": 60, "bed_type": "大床", "max_occupancy": 4, "price": 500},
    {"room_type": "家庭房", "room_name": "家庭双床房", "area": 35, "bed_type": "双床", "max_occupancy": 3, "price": 300},
]

# 攻略数据
STRATEGIES = [
    {"title": "太原1日游攻略", "category": "1day", "theme": "文化", "duration": 1, "budget": 300},
    {"title": "太原2日游攻略", "category": "2day", "theme": "休闲", "duration": 2, "budget": 600},
    {"title": "太原古建筑之旅", "category": "theme", "theme": "历史", "duration": 2, "budget": 500},
    {"title": "太原美食之旅", "category": "theme", "theme": "美食", "duration": 1, "budget": 200},
    {"title": "太原亲子游攻略", "category": "theme", "theme": "亲子", "duration": 2, "budget": 800},
    {"title": "太原摄影之旅", "category": "theme", "theme": "摄影", "duration": 2, "budget": 600},
    {"title": "太原周末游", "category": "2day", "theme": "休闲", "duration": 2, "budget": 500},
    {"title": "太原深度游", "category": "theme", "theme": "文化", "duration": 3, "budget": 1000},
    {"title": "太原秋季赏红叶", "category": "theme", "theme": "自然", "duration": 1, "budget": 300},
    {"title": "太原博物馆之旅", "category": "theme", "theme": "文化", "duration": 1, "budget": 200},
]

# 公告数据
ANNOUNCEMENTS = [
    {"title": "2024年太原旅游节盛大开幕", "category": "festival", "is_top": 1, "is_banner": 1},
    {"title": "晋祠景区门票优惠活动", "category": "promotion", "is_top": 0, "is_banner": 1},
    {"title": "太原新增多条旅游线路", "category": "news", "is_top": 0, "is_banner": 0},
    {"title": "春季旅游优惠季开始", "category": "promotion", "is_top": 0, "is_banner": 0},
    {"title": "太原古县城文化节即将举办", "category": "festival", "is_top": 0, "is_banner": 0},
]


def get_db_connection():
    """获取数据库连接"""
    return pymysql.connect(**DB_CONFIG)


def insert_attractions(conn, category_ids):
    """插入景点数据"""
    cursor = conn.cursor()
    count = 0
    
    for att in ATTRACTIONS:
        sql = """
        INSERT INTO attraction (name, category_id, description, content, cover_image, address, 
                               longitude, latitude, opening_hours, ticket_price, rating, view_count, 
                               comment_count, favorite_count, tags, status, sort_order, create_time, update_time)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        content = f"{att['description']}\n\n{att['description']}是太原市著名的旅游景点，具有深厚的历史文化底蕴。"
        rating = round(random.uniform(4.0, 5.0), 1)
        view_count = random.randint(100, 5000)
        comment_count = random.randint(10, 200)
        favorite_count = random.randint(5, 150)
        sort_order = random.randint(1, 100)
        now = datetime.now()
        
        cursor.execute(sql, (
            att['name'],
            random.choice(category_ids) if category_ids else None,
            att['description'],
            content,
            f"/uploads/images/attraction_{count+1}.jpg",
            att['address'],
            att['longitude'],
            att['latitude'],
            att['opening_hours'],
            att['ticket_price'],
            rating,
            view_count,
            comment_count,
            favorite_count,
            att['tags'],
            1,  # status: 已通过
            sort_order,
            now,
            now
        ))
        count += 1
    
    conn.commit()
    print(f"✓ 成功插入 {count} 条景点数据")
    return count


def insert_foods(conn, category_ids):
    """插入美食数据"""
    cursor = conn.cursor()
    count = 0
    
    for food in FOODS:
        sql = """
        INSERT INTO food (name, category_id, description, content, cover_image, origin, 
                         ingredients, cooking_method, rating, view_count, comment_count, 
                         favorite_count, tags, status, sort_order, create_time, update_time)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        content = f"制作方法：\n1. 准备食材：{food['ingredients']}\n2. 按照传统工艺制作\n3. 调味后即可食用"
        cooking_method = f"传统制作工艺，需要掌握火候和技巧"
        rating = round(random.uniform(4.0, 5.0), 1)
        view_count = random.randint(100, 3000)
        comment_count = random.randint(10, 150)
        favorite_count = random.randint(5, 100)
        tags = "山西,传统,美食"
        sort_order = random.randint(1, 100)
        now = datetime.now()
        
        cursor.execute(sql, (
            food['name'],
            random.choice(category_ids) if category_ids else None,
            food['description'],
            content,
            f"/uploads/images/food_{count+1}.jpg",
            food['origin'],
            food['ingredients'],
            cooking_method,
            rating,
            view_count,
            comment_count,
            favorite_count,
            tags,
            1,  # status: 已通过
            sort_order,
            now,
            now
        ))
        count += 1
    
    conn.commit()
    print(f"✓ 成功插入 {count} 条美食数据")
    return count


def insert_hotels(conn):
    """插入酒店数据"""
    cursor = conn.cursor()
    hotel_ids = []
    count = 0
    
    for hotel in HOTELS:
        sql = """
        INSERT INTO hotel (name, description, cover_image, address, longitude, latitude, 
                         phone, star_level, rating, facilities, min_price, view_count, 
                         comment_count, status, create_time, update_time)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        description = f"{hotel['name']}位于{hotel['address']}，是一家{hotel['star_level']}星级酒店，设施完善，服务优质。"
        facilities = json.dumps(["WiFi", "停车场", "餐厅", "会议室", "健身房", "游泳池"], ensure_ascii=False)
        rating = round(random.uniform(4.0, 5.0), 1)
        min_price = random.randint(150, 600)
        view_count = random.randint(200, 5000)
        comment_count = random.randint(20, 300)
        now = datetime.now()
        
        cursor.execute(sql, (
            hotel['name'],
            description,
            f"/uploads/images/hotel_{count+1}.jpg",
            hotel['address'],
            hotel['longitude'],
            hotel['latitude'],
            hotel['phone'],
            hotel['star_level'],
            rating,
            facilities,
            min_price,
            view_count,
            comment_count,
            1,  # status: 上架
            now,
            now
        ))
        hotel_id = cursor.lastrowid
        hotel_ids.append(hotel_id)
        count += 1
        
        # 为每个酒店插入2-3个房间
        num_rooms = random.randint(2, 3)
        for i in range(num_rooms):
            room = random.choice(ROOM_TYPES)
            room_sql = """
            INSERT INTO hotel_room (hotel_id, room_type, room_name, description, image, area, 
                                   bed_type, max_occupancy, facilities, price, stock, status, 
                                   create_time, update_time)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """
            room_description = f"{room['room_name']}，面积{room['area']}平方米，{room['bed_type']}，可住{room['max_occupancy']}人"
            room_facilities = json.dumps(["WiFi", "电视", "空调", "独立卫浴", "免费洗漱用品"], ensure_ascii=False)
            room_price = room['price'] * hotel['star_level'] // 2  # 根据星级调整价格
            
            cursor.execute(room_sql, (
                hotel_id,
                room['room_type'],
                room['room_name'],
                room_description,
                f"/uploads/images/room_{count}_{i+1}.jpg",
                room['area'],
                room['bed_type'],
                room['max_occupancy'],
                room_facilities,
                room_price,
                random.randint(5, 20),  # stock
                1,  # status: 上架
                now,
                now
            ))
    
    conn.commit()
    print(f"✓ 成功插入 {count} 条酒店数据（包含房间）")
    return count


def insert_strategies(conn):
    """插入攻略数据"""
    cursor = conn.cursor()
    count = 0
    
    for strategy in STRATEGIES:
        sql = """
        INSERT INTO strategy (title, category, theme, cover_image, description, content, 
                            duration, budget, best_season, tips, view_count, comment_count, 
                            favorite_count, like_count, status, is_recommend, create_time, update_time)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        description = f"{strategy['title']}，适合{strategy['theme']}主题，预计{strategy['duration']}天，预算约{strategy['budget']}元。"
        content = f"""
        【行程安排】
        第一天：游览主要景点，体验当地文化
        第二天：继续深度游览（如适用）
        
        【景点推荐】
        根据主题选择适合的景点进行游览
        
        【美食推荐】
        品尝当地特色美食
        
        【住宿建议】
        选择交通便利的酒店
        
        【注意事项】
        请提前预订门票和酒店，注意天气变化
        """
        best_season = random.choice(["春季", "夏季", "秋季", "冬季"])
        tips = "建议提前规划行程，注意天气变化，携带必要的证件和物品。"
        view_count = random.randint(500, 5000)
        comment_count = random.randint(10, 200)
        favorite_count = random.randint(20, 300)
        like_count = random.randint(30, 400)
        is_recommend = 1 if random.random() > 0.3 else 0
        now = datetime.now()
        
        cursor.execute(sql, (
            strategy['title'],
            strategy['category'],
            strategy['theme'],
            f"/uploads/images/strategy_{count+1}.jpg",
            description,
            content,
            strategy['duration'],
            strategy['budget'],
            best_season,
            tips,
            view_count,
            comment_count,
            favorite_count,
            like_count,
            1,  # status: 已通过
            is_recommend,
            now,
            now
        ))
        count += 1
    
    conn.commit()
    print(f"✓ 成功插入 {count} 条攻略数据")
    return count


def insert_announcements(conn):
    """插入公告数据"""
    cursor = conn.cursor()
    count = 0
    
    for ann in ANNOUNCEMENTS:
        sql = """
        INSERT INTO announcement (title, category, cover_image, content, start_time, end_time, 
                                is_top, is_banner, view_count, status, sort_order, create_time, update_time)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        content = f"{ann['title']}的详细内容。欢迎广大游客前来参观游览。"
        start_time = datetime.now() - timedelta(days=random.randint(0, 30))
        end_time = start_time + timedelta(days=random.randint(30, 90))
        view_count = random.randint(100, 2000)
        sort_order = random.randint(1, 100)
        now = datetime.now()
        
        cursor.execute(sql, (
            ann['title'],
            ann['category'],
            f"/uploads/images/announcement_{count+1}.jpg",
            content,
            start_time,
            end_time,
            ann['is_top'],
            ann['is_banner'],
            view_count,
            1,  # status: 上架
            sort_order,
            now,
            now
        ))
        count += 1
    
    conn.commit()
    print(f"✓ 成功插入 {count} 条公告数据")
    return count


def get_category_ids(conn, table_name):
    """获取分类ID列表"""
    cursor = conn.cursor()
    cursor.execute(f"SELECT id FROM {table_name} LIMIT 10")
    results = cursor.fetchall()
    return [row[0] for row in results] if results else []


def main():
    """主函数"""
    print("=" * 60)
    print("旅游平台示例数据插入脚本")
    print("=" * 60)
    
    try:
        # 连接数据库
        conn = get_db_connection()
        print("✓ 数据库连接成功\n")
        
        # 获取分类ID
        attraction_category_ids = get_category_ids(conn, "attraction_category")
        food_category_ids = get_category_ids(conn, "food_category")
        
        total_count = 0
        
        # 插入数据
        print("开始插入数据...\n")
        
        count = insert_attractions(conn, attraction_category_ids)
        total_count += count
        
        count = insert_foods(conn, food_category_ids)
        total_count += count
        
        count = insert_hotels(conn)
        total_count += count
        
        count = insert_strategies(conn)
        total_count += count
        
        count = insert_announcements(conn)
        total_count += count
        
        print("\n" + "=" * 60)
        print(f"✓ 数据插入完成！共插入 {total_count} 条主要数据")
        print("=" * 60)
        
        conn.close()
        
    except Exception as e:
        print(f"\n✗ 错误: {str(e)}")
        import traceback
        traceback.print_exc()


if __name__ == "__main__":
    main()
