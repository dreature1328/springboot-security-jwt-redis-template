import os
import json
import uuid
import random
import time
from datetime import datetime, timedelta

def generate_mock_user_data(count):
    data = []
    # 常用域名列表
    email_domains = ['gmail.com', 'yahoo.com', 'hotmail.com', 'example.com', 'qq.com', '163.com']
    # 中文姓氏列表
    chinese_surnames = ['王', '李', '张', '刘', '陈', '杨', '赵', '黄', '周', '吴']
    # 中文名字列表
    chinese_names = ['伟', '芳', '娜', '秀英', '敏', '静', '丽', '强', '磊', '军', '洋', '艳', '勇', '杰', '娟', '涛']
    # 英文名字列表
    english_names = ['James', 'John', 'Robert', 'Michael', 'William', 'David', 'Richard', 'Joseph', 'Thomas', 'Mary', 
                    'Patricia', 'Jennifer', 'Linda', 'Elizabeth', 'Barbara', 'Susan', 'Jessica', 'Sarah', 'Karen']
    
    for i in range(count):
        # 用户ID (自增)
        user_id = i + 1
        
        # UUID
        user_uuid = str(uuid.uuid4())
        
        # 用户名 (字母+数字组合)
        username = f"user{random.randint(1000, 9999)}"
        
        # 密码哈希 (模拟)
        password_hash = f"${random.choice(['2a', '2b', '2y'])}${random.randint(10, 12)}${uuid.uuid4().hex[:22]}{uuid.uuid4().hex[:31]}"
        
        # 邮箱 (随机组合)
        email_local = ''.join(random.choices('abcdefghijklmnopqrstuvwxyz1234567890', k=random.randint(5, 10)))
        email = f"{email_local}@{random.choice(email_domains)}"
        
        # 手机号 (中国手机号格式)
        phone = f"1{random.choice(['3', '5', '7', '8', '9'])}{random.randint(100000000, 999999999)}"
        
        # 昵称 (中英文混合)
        if random.random() < 0.7:  # 70%概率中文昵称
            nickname = f"{random.choice(chinese_surnames)}{random.choice(chinese_names)}"
        else:
            nickname = random.choice(english_names)
            if random.random() < 0.3:  # 30%概率添加数字后缀
                nickname += str(random.randint(1, 99))
        
        # 头像URL
        avatar_url = f"https://avatar.example.com/{uuid.uuid4().hex[:8]}.jpg"
        
        # 性别
        gender = random.choice(['M', 'F', 'O', 'U'])
        
        # 出生日期 (18-80岁之间)
        if random.random() < 0.9:  # 90%概率有出生日期
            birth_year = datetime.now().year - random.randint(1, 50)
            birth_month = random.randint(1, 12)
            birth_day = random.randint(1, 28)  # 避免2月29日问题
            # 修正时间戳计算错误
            birth_date = int(datetime(birth_year, birth_month, birth_day).timestamp()) * 1000
        else:
            birth_date = None
        
        # 当前时间戳（毫秒）
        current_ts = int(time.time() * 1000)
        
        # 创建时间 (过去1-5年内)
        created_days_ago = random.randint(1, 5 * 365)
        created_at = current_ts - (created_days_ago * 24 * 60 * 60 * 1000)
        
        # 更新时间 (创建时间之后，可能在最后登录之前或之后)
        updated_days_ago = random.randint(0, created_days_ago)
        updated_at = created_at + (updated_days_ago * 24 * 60 * 60 * 1000)
        
        # 最后登录时间 (50%概率有登录记录)
        if random.random() < 0.5:
            login_days_ago = random.randint(0, min(created_days_ago, 365))  # 最后登录在1年内
            last_login_at = current_ts - (login_days_ago * 24 * 60 * 60 * 1000)
        else:
            last_login_at = None
        
        status = random.choices(
            ['ACTIVE', 'LOCKED', 'DISABLED', 'PENDING'],
            weights=[0.7, 0.1, 0.1, 0.1]  # 不同状态的权重
        )[0]
        
        data.append({
            "id": user_id,
            "uuid": user_uuid,
            "username": username,
            "passwordHash": password_hash,
            "email": email,
            "phone": phone,
            "nickname": nickname,
            "avatarUrl": avatar_url,
            "gender": gender,
            "birthDate": birth_date,
            "status": status,
            "lastLoginAt": last_login_at,
            "createdAt": created_at,
            "updatedAt": updated_at
        })
    
    return data

def create_response(data):
    return {
        "success": True,
        "code": "SUCCESS",
        "message": "操作成功",
        "data": data,
        "timestamp": int(time.time() * 1000)  # 毫秒级时间戳
    }

def save_to_file(data, filename):
    current_dir = os.path.dirname(os.path.abspath(__file__))
    output_path = os.path.join(current_dir, filename)
    
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(data, f, ensure_ascii=False, indent=2)
    
    print(f"保存文件: {output_path}")
    print(f"生成时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print(f"用户数量: {len(data['data'])}")

if __name__ == '__main__':
    # 生成用户数据
    user_count = 500
    mock_user_data = generate_mock_user_data(user_count)
    
    # 创建响应结构
    response = create_response(mock_user_data)
    
    # 保存到文件
    save_to_file(response, 'mock_users.json')