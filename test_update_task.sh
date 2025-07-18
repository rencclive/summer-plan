#!/bin/bash

# 测试更新任务API

TASK_ID=${1:-1}

# 登录获取token
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }')
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
if [ -z "$TOKEN" ]; then
    echo "登录失败，无法获取token"
    echo "登录响应: $LOGIN_RESPONSE"
    exit 1
fi

echo "获取到token: $TOKEN"

echo "发送更新任务请求..."
curl -X PUT http://localhost:8080/api/tasks/$TASK_ID \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "更新后的任务",
    "description": "更新描述",
    "dueDate": "2024-07-15",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "progress": 50
  }' \
  -w "\nHTTP状态码: %{http_code}\n" 