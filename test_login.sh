#!/bin/bash

echo "测试登录API..."

# 测试登录
echo "发送登录请求..."
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }' \
  -w "\nHTTP状态码: %{http_code}\n"

echo ""
echo "测试完成" 