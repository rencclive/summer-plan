#!/bin/bash

# 测试删除计划API

if [ -z "$1" ]; then
  echo "用法: $0 <计划ID>"
  exit 1
fi
PLAN_ID=$1

# 登录获取token
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "chaoren111",
    "password": "111"
  }')
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
if [ -z "$TOKEN" ]; then
    echo "登录失败，无法获取token"
    echo "登录响应: $LOGIN_RESPONSE"
    exit 1
fi

echo "获取到token: $TOKEN"

echo "发送删除计划请求..."
curl -X DELETE http://localhost:8080/api/plans/$PLAN_ID \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -w "\nHTTP状态码: %{http_code}\n" 