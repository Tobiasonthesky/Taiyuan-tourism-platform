# -*- coding: utf-8 -*-
"""
旅游平台测试数据插入脚本
向系统每个模块插入测试数据，使用在线图片链接
"""

import pymysql
from datetime import datetime, timedelta
import random

# 数据库配置
DB_CONFIG = {
    'host': 'localhost',
    'port': 3306,
    'user': 'root',
    'password': '123456',
    'database': 'tourism_platform',
    'charset': 'utf8mb4'
}

# 在线图片链接库
IMAGE_URLS = [
    'https://images.pexels.com/photos/8540878/pexels-photo-8540878.jpeg',
    'https://images.pexels.com/photos/35464172/pexels-photo-35464172.jpeg',
    'https://images.pexels.com/photos/1010657/pexels-photo-1010657.jpeg',
    'https://images.pexels.com/photos/35199314/pexels-photo-35199314.jpeg',
    'https://images.pexels.com/photos/35134367/pexels-photo-35134367.jpeg',
    'https://images.pexels.com/photos/14686356/pexels-photo-14686356.jpeg',
    'https://images.pexels.com/photos/16197273/pexels-photo-16197273.jpeg',
    'https://images.pexels.com/photos/34922354/pexels-photo-34922354.jpeg',
    'https://images.pexels.com/photos/34652802/pexels-photo-34652802.jpeg',
    'https://images.pexels.com/photos/13009050/pexels-photo-13009050.jpeg',
    'https://images.pexels.com/photos/12274675/pexels-photo-12274675.jpeg',
    'https://images.pexels.com/photos/33713697/pexels-photo-33713697.jpeg',
    'https://images.pexels.com/photos/31894060/pexels-photo-31894060.jpeg',
    'https://images.pexels.com/photos/35221905/pexels-photo-35221905.jpeg',
    'https://images.pexels.com/photos/618491/pexels-photo-618491.jpeg',
    'https://images.pexels.com/photos/734102/pexels-photo-734102.jpeg',
    'https://images.pexels.com/photos/235731/pexels-photo-235731.jpeg',
    'https://images.pexels.com/photos/35163027/pexels-photo-35163027.jpeg',
]

def get_connection():
    """获取数据库连接"""
    return pymysql.connect(**DB_CONFIG)

def insert_announcements(cursor):
    """插入活动公告数据"""
    print("[INFO] 正在插入活动公告数据...")
    
    announcements = [
        {
            'title': '2026 年太原旅游文化节盛大开幕',
            'category': 'festival',
            'cover_image': random.choice(IMAGE_URLS),
            'content': '2026 年太原旅游文化节将于 5 月 1 日正式开幕，活动期间将有精彩的民俗表演、美食展览、旅游产品展销等丰富多彩的内容。欢迎广大游客前来参与！',
            'start_time': '2026-05-01 09:00:00',
            'end_time': '2026-05-07 18:00:00',
            'is_top': 1,
            'is_banner': 1,
            'view_count': random.randint(1000, 5000),
            'status': 1,
            'sort_order': 1
        },
        {
            'title': '五一假期景区门票优惠活动',
            'category': 'promotion',
            'cover_image': random.choice(IMAGE_URLS),
            'content': '五一劳动节期间，太原市主要景区推出门票优惠活动，晋祠、双塔寺、天龙山石窟等景点门票 8 折优惠。活动时间：5 月 1 日 -5 月 5 日。',
            'start_time': '2026-05-01 00:00:00',
            'end_time': '2026-05-05 23:59:59',
            'is_top': 0,
            'is_banner': 1,
            'view_count': random.randint(500, 3000),
            'status': 1,
            'sort_order': 2
        },
        {
            'title': '太原美食街新开业商家入驻',
            'category': 'news',
            'cover_image': random.choice(IMAGE_URLS),
            'content': '太原食品街迎来新一轮商家入驻，新增特色小吃店铺 30 余家，包括山西传统面食、各地特色小吃等，为游客提供更加丰富的美食选择。',
            'start_time': '2026-04-20 00:00:00',
            'end_time': '2026-12-31 23:59:59',
            'is_top': 0,
            'is_banner': 0,
            'view_count': random.randint(200, 1500),
            'status': 1,
            'sort_order': 3
        }
    ]
    
    for ann in announcements:
        sql = """
        INSERT INTO announcement (title, category, cover_image, content, start_time, end_time, 
                                   is_top, is_banner, view_count, status, sort_order)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        cursor.execute(sql, (
            ann['title'], ann['category'], ann['cover_image'], ann['content'],
            ann['start_time'], ann['end_time'], ann['is_top'], ann['is_banner'],
            ann['view_count'], ann['status'], ann['sort_order']
        ))
    
    print(f"  [OK] 插入 {len(announcements)} 条公告数据")

def insert_attractions(cursor):
    """插入景点数据"""
    print("[INFO] 正在插入景点数据...")
    
    attractions = [
        {
            'name': '晋阳湖公园',
            'category_id': 1,
            'description': '华北地区最大的人工湖公园，集休闲、娱乐、观光于一体',
            'content': '晋阳湖公园位于太原市晋源区，总面积约 27 平方公里，其中水域面积约 4.5 平方公里。公园以晋阳湖为核心，建有环湖步道、观景平台、儿童游乐场等设施，是太原市重要的生态休闲景区。',
            'cover_image': random.choice(IMAGE_URLS),
            'address': '山西省太原市晋源区晋源街道',
            'longitude': 112.4650000,
            'latitude': 37.7250000,
            'opening_hours': '全天开放',
            'ticket_price': 0.00,
            'rating': round(random.uniform(4.5, 5.0), 2),
            'view_count': random.randint(5000, 15000),
            'tags': '自然风光，湖泊，休闲',
            'status': 1,
            'sort_order': 100
        },
        {
            'name': '永祚寺双塔',
            'category_id': 3,
            'description': '太原标志性建筑，明代古塔，登塔可俯瞰太原全景',
            'content': '永祚寺双塔又称双塔寺，建于明代万历年间，两塔相距约 60 米，均为十三层楼阁式砖塔，高约 54 米。双塔凌霄是太原八景之一，也是太原的城市标志。',
            'cover_image': random.choice(IMAGE_URLS),
            'address': '山西省太原市迎泽区郝庄镇郝庄村',
            'longitude': 112.5801200,
            'latitude': 37.8439800,
            'opening_hours': '08:00-17:30',
            'ticket_price': 30.00,
            'rating': round(random.uniform(4.3, 4.8), 2),
            'view_count': random.randint(3000, 10000),
            'tags': '历史古迹，佛教文化，地标',
            'status': 1,
            'sort_order': 99
        },
        {
            'name': '太原方特东方神画',
            'category_id': 4,
            'description': '大型文化科技主题乐园，融合华夏历史文化与高科技娱乐项目',
            'content': '太原方特东方神画是华强方特集团在山西打造的大型文化科技主题乐园，以中华历史文化传承为主题，运用高科技手段，打造了一系列室内室外游乐项目，适合亲子游玩。',
            'cover_image': random.choice(IMAGE_URLS),
            'address': '山西省太原市阳曲县华强路 1 号',
            'longitude': 112.7150000,
            'latitude': 38.0650000,
            'opening_hours': '09:30-17:30',
            'ticket_price': 280.00,
            'rating': round(random.uniform(4.6, 4.9), 2),
            'view_count': random.randint(8000, 20000),
            'tags': '主题乐园，亲子，娱乐',
            'status': 1,
            'sort_order': 98
        }
    ]
    
    for att in attractions:
        sql = """
        INSERT INTO attraction (name, category_id, description, content, cover_image, address, 
                                longitude, latitude, opening_hours, ticket_price, rating, 
                                view_count, tags, status, sort_order)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        cursor.execute(sql, (
            att['name'], att['category_id'], att['description'], att['content'],
            att['cover_image'], att['address'], att['longitude'], att['latitude'],
            att['opening_hours'], att['ticket_price'], att['rating'], att['view_count'],
            att['tags'], att['status'], att['sort_order']
        ))
    
    print(f"  [OK] 插入 {len(attractions)} 条景点数据")

def insert_foods(cursor):
    """插入美食数据"""
    print("正在插入美食数据...")
    
    foods = [
        {
            'name': '莜面栲栳栳',
            'category_id': 4,
            'description': '山西特色面食，形状像圆筒，口感筋道',
            'content': '莜面栲栳栳是山西传统面食，用莜面制成圆筒状，蒸熟后蘸料食用。口感筋道，营养丰富，是山西人餐桌上的常见美食。',
            'cover_image': random.choice(IMAGE_URLS),
            'origin': '山西传统面食，历史悠久',
            'ingredients': '莜面、水',
            'cooking_method': '将莜面和成面团，搓成小圆筒状，蒸熟后蘸料食用',
            'rating': round(random.uniform(4.4, 4.8), 2),
            'view_count': random.randint(2000, 8000),
            'tags': '山西，传统，美食',
            'status': 1,
            'sort_order': 100
        },
        {
            'name': '羊杂割',
            'category_id': 1,
            'description': '山西特色小吃，汤鲜味美，营养丰富',
            'content': '羊杂割是山西传统小吃，用羊杂碎熬制而成，汤色乳白，味道鲜美。食用时配以香菜、辣椒油等调料，是太原人早餐的常见选择。',
            'cover_image': random.choice(IMAGE_URLS),
            'origin': '山西传统小吃，有数百年历史',
            'ingredients': '羊杂、羊骨汤、香菜、辣椒油',
            'cooking_method': '将羊杂碎处理干净，用羊骨汤熬制，配以调料食用',
            'rating': round(random.uniform(4.5, 4.9), 2),
            'view_count': random.randint(3000, 10000),
            'tags': '山西，传统，小吃',
            'status': 1,
            'sort_order': 99
        },
        {
            'name': '认一力蒸饺',
            'category_id': 2,
            'description': '太原老字号，皮薄馅大，味道鲜美',
            'content': '认一力是太原著名的清真老字号，以蒸饺闻名。其蒸饺皮薄馅大，汤汁丰富，配以特制蘸料，深受太原本地人喜爱。',
            'cover_image': random.choice(IMAGE_URLS),
            'origin': '太原老字号，始创于 1930 年',
            'ingredients': '面粉、牛肉、羊肉、蔬菜',
            'cooking_method': '传统手工包制，蒸制而成',
            'rating': round(random.uniform(4.6, 4.9), 2),
            'view_count': random.randint(4000, 12000),
            'tags': '老字号，清真，太原',
            'status': 1,
            'sort_order': 98
        }
    ]
    
    for food in foods:
        sql = """
        INSERT INTO food (name, category_id, description, content, cover_image, origin, 
                         ingredients, cooking_method, rating, view_count, tags, status, sort_order)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        cursor.execute(sql, (
            food['name'], food['category_id'], food['description'], food['content'],
            food['cover_image'], food['origin'], food['ingredients'], food['cooking_method'],
            food['rating'], food['view_count'], food['tags'], food['status'], food['sort_order']
        ))
    
    print(f"  [OK] 插入 {len(foods)} 条美食数据")

def insert_hotels(cursor):
    """插入酒店数据"""
    print("正在插入酒店数据...")
    
    hotels = [
        {
            'name': '太原万达文华酒店',
            'description': '豪华五星级酒店，设施完善，服务优质',
            'cover_image': random.choice(IMAGE_URLS),
            'address': '山西省太原市杏花岭区解放路 169 号',
            'longitude': 112.5780000,
            'latitude': 37.8880000,
            'phone': '0351-8888888',
            'star_level': 5,
            'rating': round(random.uniform(4.7, 4.9), 2),
            'min_price': 600.00,
            'view_count': random.randint(3000, 10000),
            'status': 1
        },
        {
            'name': '太原洲际酒店',
            'description': '国际品牌五星级酒店，位于长风商务区',
            'cover_image': random.choice(IMAGE_URLS),
            'address': '山西省太原市万柏林区长风商务区广经路 1 号',
            'longitude': 112.5350000,
            'latitude': 37.8050000,
            'phone': '0351-8999999',
            'star_level': 5,
            'rating': round(random.uniform(4.8, 5.0), 2),
            'min_price': 800.00,
            'view_count': random.randint(2000, 8000),
            'status': 1
        },
        {
            'name': '太原富力铂尔曼酒店',
            'description': '高端商务酒店，交通便利，设施现代',
            'cover_image': random.choice(IMAGE_URLS),
            'address': '山西省太原市杏花岭区北大街 23 号',
            'longitude': 112.5650000,
            'latitude': 37.8850000,
            'phone': '0351-8777777',
            'star_level': 5,
            'rating': round(random.uniform(4.6, 4.8), 2),
            'min_price': 500.00,
            'view_count': random.randint(2500, 9000),
            'status': 1
        }
    ]
    
    for hotel in hotels:
        sql = """
        INSERT INTO hotel (name, description, cover_image, address, longitude, latitude, 
                          phone, star_level, rating, min_price, view_count, status)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        cursor.execute(sql, (
            hotel['name'], hotel['description'], hotel['cover_image'], hotel['address'],
            hotel['longitude'], hotel['latitude'], hotel['phone'], hotel['star_level'],
            hotel['rating'], hotel['min_price'], hotel['view_count'], hotel['status']
        ))
    
    print(f"  [OK] 插入 {len(hotels)} 条酒店数据")

def insert_cultures(cursor):
    """插入民俗文化数据"""
    print("正在插入民俗文化数据...")
    
    cultures = [
        {
            'name': '太原社火表演',
            'category_id': 2,
            'description': '太原传统民俗活动，春节期间举办，热闹非凡',
            'content': '社火是太原地区春节期间的传统民俗活动，包括舞龙、舞狮、踩高跷、跑旱船等表演形式，场面热闹，深受群众喜爱。',
            'cover_image': random.choice(IMAGE_URLS),
            'history': '社火表演在太原地区有数百年历史，是民间祭祀和娱乐活动的重要组成部分',
            'activity_time': '每年春节期间（正月初一至十五）',
            'activity_location': '山西省太原市各区县',
            'longitude': 112.5489300,
            'latitude': 37.8756200,
            'rating': round(random.uniform(4.5, 4.9), 2),
            'view_count': random.randint(1000, 5000),
            'tags': '民俗活动，春节，传统',
            'status': 1,
            'sort_order': 100
        },
        {
            'name': '太原莲花落',
            'category_id': 4,
            'description': '山西传统曲艺形式，说唱结合，幽默风趣',
            'content': '莲花落是山西传统曲艺，表演者手持竹板，边打边唱，内容多为民间故事、历史传说，语言生动幽默，深受群众喜爱。',
            'cover_image': random.choice(IMAGE_URLS),
            'history': '莲花落在山西地区流传已久，是省级非物质文化遗产',
            'activity_time': '常年有演出，周末较多',
            'activity_location': '山西省太原市迎泽区工人文化宫',
            'longitude': 112.5567800,
            'latitude': 37.8678900,
            'rating': round(random.uniform(4.4, 4.8), 2),
            'view_count': random.randint(800, 3000),
            'tags': '曲艺，民间艺术，非遗',
            'status': 1,
            'sort_order': 99
        }
    ]
    
    for culture in cultures:
        sql = """
        INSERT INTO culture (name, category_id, description, content, cover_image, history, 
                            activity_time, activity_location, longitude, latitude, rating, 
                            view_count, tags, status, sort_order)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        cursor.execute(sql, (
            culture['name'], culture['category_id'], culture['description'], culture['content'],
            culture['cover_image'], culture['history'], culture['activity_time'],
            culture['activity_location'], culture['longitude'], culture['latitude'],
            culture['rating'], culture['view_count'], culture['tags'], culture['status'],
            culture['sort_order']
        ))
    
    print(f"  [OK] 插入 {len(cultures)} 条民俗文化数据")

def insert_experiences(cursor):
    """插入体验项目数据"""
    print("正在插入体验项目数据...")
    
    experiences = [
        {
            'name': '老陈醋酿造体验',
            'description': '参观醋厂，了解山西老陈醋传统酿造工艺',
            'content': '在专业师傅指导下，了解老陈醋的原料选择、发酵、陈酿等工艺流程，亲手参与部分环节，品尝不同年份的陈醋，感受山西醋文化的魅力。',
            'cover_image': random.choice(IMAGE_URLS),
            'address': '山西省太原市清徐县老陈醋文化园',
            'longitude': 112.3456700,
            'latitude': 37.6789000,
            'duration': 180,
            'price': 168.00,
            'rating': round(random.uniform(4.6, 4.9), 2),
            'view_count': random.randint(1000, 5000),
            'tags': '文化体验，美食，非遗',
            'status': 1,
            'sort_order': 100
        },
        {
            'name': '太原锣鼓学习体验',
            'description': '学习山西民间打击乐，感受锣鼓文化的魅力',
            'content': '由专业锣鼓艺人教学，学习太原锣鼓的基本节奏和演奏技巧，了解锣鼓文化的历史渊源，体验山西民间艺术的独特魅力。',
            'cover_image': random.choice(IMAGE_URLS),
            'address': '山西省太原市晋源区晋祠公园',
            'longitude': 112.4522300,
            'latitude': 37.7964500,
            'duration': 120,
            'price': 128.00,
            'rating': round(random.uniform(4.5, 4.8), 2),
            'view_count': random.randint(800, 3500),
            'tags': '民俗活动，音乐，传统',
            'status': 1,
            'sort_order': 99
        }
    ]
    
    for exp in experiences:
        sql = """
        INSERT INTO experience (name, description, content, cover_image, address, longitude, 
                               latitude, duration, price, rating, view_count, tags, status, sort_order)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        cursor.execute(sql, (
            exp['name'], exp['description'], exp['content'], exp['cover_image'],
            exp['address'], exp['longitude'], exp['latitude'], exp['duration'],
            exp['price'], exp['rating'], exp['view_count'], exp['tags'],
            exp['status'], exp['sort_order']
        ))
    
    print(f"  [OK] 插入 {len(experiences)} 条体验项目数据")

def insert_strategies(cursor):
    """插入旅游攻略数据"""
    print("正在插入旅游攻略数据...")
    
    strategies = [
        {
            'title': '太原春季赏花三日游',
            'user_id': 3,
            'category': 'theme',
            'theme': '赏花',
            'cover_image': random.choice(IMAGE_URLS),
            'description': '春季是太原最美的季节，本攻略带你游览太原植物园、汾河公园、迎泽公园等地，欣赏郁金香、樱花、海棠等各类花卉，感受龙城春天的魅力。',
            'content': '''
**第一天：太原植物园赏花**
- 上午：前往太原植物园，参观热带雨林馆、沙生植物馆
- 中午：在植物园附近用餐
- 下午：重点游览室外园区，欣赏郁金香、牡丹等花卉
- 晚上：返回市区，品尝山西面食

**第二天：市区公园漫步**
- 上午：游览迎泽公园，观赏樱花和海棠
- 中午：在柳巷食品街品尝小吃
- 下午：前往汾河公园，沿河骑行赏花
- 晚上：参观钟楼街夜景

**第三天：晋祠寻古**
- 上午：游览晋祠博物馆，欣赏古树名木
- 中午：在晋祠附近用餐
- 下午：前往天龙山石窟，感受历史文化
- 晚上：结束行程，准备返程

**预算分配**：
- 门票：约 200 元
- 餐饮：约 300 元
- 交通：约 100 元
- 住宿：约 400 元（2 晚）
总计：约 1000 元
            ''',
            'duration': 3,
            'budget': 1000.00,
            'best_season': '春季（4 月 -5 月）',
            'tips': '1. 春季太原温差较大，建议携带外套；2. 植物园面积较大，建议穿舒适的鞋子；3. 周末游客较多，建议错峰出行；4. 部分花卉花期较短，出行前请查询花期信息。',
            'view_count': random.randint(2000, 8000),
            'favorite_count': random.randint(100, 500),
            'like_count': random.randint(200, 800),
            'status': 1,
            'is_recommend': 1
        },
        {
            'title': '太原历史文化深度两日游',
            'user_id': 3,
            'category': '2day',
            'theme': '文化',
            'cover_image': random.choice(IMAGE_URLS),
            'description': '深入探索太原三千年历史文化，游览晋祠、双塔寺、山西博物院等文化地标，感受三晋大地的深厚文化底蕴。',
            'content': '''
**第一天：古建探秘**
- 上午：游览晋祠博物馆，参观圣母殿、鱼沼飞梁
- 中午：品尝晋祠附近农家菜
- 下午：前往天龙山石窟，欣赏佛教艺术
- 晚上：入住市区酒店

**第二天：城市记忆**
- 上午：参观山西博物院，了解三晋文化
- 中午：在食品街品尝太原小吃
- 下午：游览双塔寺，登塔俯瞰太原
- 晚上：参观太原古县城，结束行程

**预算分配**：
- 门票：约 250 元
- 餐饮：约 200 元
- 交通：约 80 元
- 住宿：约 300 元（1 晚）
总计：约 830 元
            ''',
            'duration': 2,
            'budget': 830.00,
            'best_season': '全年皆宜',
            'tips': '1. 博物院需提前预约，周一闭馆；2. 晋祠建议请讲解员；3. 双塔寺台阶较陡，注意安全；4. 古县城晚上有灯光秀，值得一看。',
            'view_count': random.randint(1500, 6000),
            'favorite_count': random.randint(80, 400),
            'like_count': random.randint(150, 600),
            'status': 1,
            'is_recommend': 1
        }
    ]
    
    for strategy in strategies:
        sql = """
        INSERT INTO strategy (title, user_id, category, theme, cover_image, description, 
                             content, duration, budget, best_season, tips, view_count, 
                             favorite_count, like_count, status, is_recommend)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        cursor.execute(sql, (
            strategy['title'], strategy['user_id'], strategy['category'], strategy['theme'],
            strategy['cover_image'], strategy['description'], strategy['content'],
            strategy['duration'], strategy['budget'], strategy['best_season'], strategy['tips'],
            strategy['view_count'], strategy['favorite_count'], strategy['like_count'],
            strategy['status'], strategy['is_recommend']
        ))
    
    print(f"  [OK] 插入 {len(strategies)} 条攻略数据")

def main():
    """主函数"""
    print("-" * 60)
    print("旅游平台测试数据插入脚本")
    print("-" * 60)
    print()
    
    try:
        # 连接数据库
        print("正在连接数据库...")
        conn = get_connection()
        cursor = conn.cursor()
        print("  [OK] 数据库连接成功")
        print()
        
        # 插入各模块数据
        insert_announcements(cursor)
        insert_attractions(cursor)
        insert_foods(cursor)
        insert_hotels(cursor)
        insert_cultures(cursor)
        insert_experiences(cursor)
        insert_strategies(cursor)
        
        # 提交事务
        conn.commit()
        print()
        print("=" * 60)
        print("[OK] 所有数据插入完成！")
        print("=" * 60)
        
    except Exception as e:
        print(f"\n[ERROR] 发生错误：{e}")
        conn.rollback()
    finally:
        if conn:
            cursor.close()
            conn.close()
            print("\n数据库连接已关闭")

if __name__ == '__main__':
    main()
