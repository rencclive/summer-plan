#!/bin/bash

# 测试创建新计划API

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

echo "发送创建计划请求..."
curl -X POST http://localhost:8080/api/plans \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "测试计划",
    "description": "测试计划描述",
    "startDate": "2024-07-01",
    "endDate": "2024-08-31",
    "status": "DRAFT",
    "priority": "MEDIUM",
    "progress": 0,
    "tags": "测试,暑假"
  }' \
  -w "\nHTTP状态码: %{http_code}\n" 