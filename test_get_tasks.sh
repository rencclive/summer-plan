#!/bin/bash

# 测试获取计划下所有任务API

PLAN_ID=${1:-1}

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

echo "发送获取计划下所有任务请求..."
curl -X GET http://localhost:8080/api/plans/$PLAN_ID/tasks \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -w "\nHTTP状态码: %{http_code}\n" 