#!/bin/bash

echo "=== SummerPlan Manager API 综合测试 ==="
echo ""

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 测试计数器
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# 测试函数
run_test() {
    local test_name="$1"
    local command="$2"
    local expected_status="$3"
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    echo -e "${YELLOW}测试 $test_name...${NC}"
    
    # 执行命令并捕获状态码
    local response=$(eval "$command")
    local status_code=$(echo "$response" | tail -n1 | grep -o '[0-9]*$')
    
    if [ "$status_code" = "$expected_status" ]; then
        echo -e "${GREEN}✓ 通过 (状态码: $status_code)${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
    else
        echo -e "${RED}✗ 失败 (期望: $expected_status, 实际: $status_code)${NC}"
        echo "响应: $response"
        FAILED_TESTS=$((FAILED_TESTS + 1))
    fi
    echo ""
}

# 1. 测试用户注册
echo "=== 1. 用户注册测试 ==="
run_test "用户注册" \
    'curl -s -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '"'"'{"username": "testuser1", "password": "test123"}'"'"' -w "\nHTTP状态码: %{http_code}"' \
    "200"

run_test "重复注册（应该失败）" \
    'curl -s -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '"'"'{"username": "testuser1", "password": "test123"}'"'"' -w "\nHTTP状态码: %{http_code}"' \
    "400"

# 2. 测试用户登录
echo "=== 2. 用户登录测试 ==="
run_test "使用admin用户登录" \
    'curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '"'"'{"username": "admin", "password": "admin123"}'"'"' -w "\nHTTP状态码: %{http_code}"' \
    "200"

run_test "使用错误密码登录" \
    'curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '"'"'{"username": "admin", "password": "wrongpassword"}'"'"' -w "\nHTTP状态码: %{http_code}"' \
    "400"

# 3. 获取token用于后续测试
echo "=== 3. 获取认证token ==="
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }')

TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
    echo -e "${RED}无法获取token，跳过需要认证的测试${NC}"
    echo "登录响应: $LOGIN_RESPONSE"
else
    echo -e "${GREEN}成功获取token: ${TOKEN:0:20}...${NC}"
    echo ""
    
    # 4. 测试需要认证的接口
    echo "=== 4. 需要认证的接口测试 ==="
    run_test "获取当前用户信息 (/me)" \
        'curl -s -X GET http://localhost:8080/api/auth/me -H "Authorization: Bearer '"$TOKEN"'" -H "Content-Type: application/json" -w "\nHTTP状态码: %{http_code}"' \
        "200"
    
    run_test "获取用户详细信息 (/profile)" \
        'curl -s -X GET http://localhost:8080/api/auth/profile -H "Authorization: Bearer '"$TOKEN"'" -H "Content-Type: application/json" -w "\nHTTP状态码: %{http_code}"' \
        "200"
    
    run_test "使用无效token访问受保护接口" \
        'curl -s -X GET http://localhost:8080/api/auth/me -H "Authorization: Bearer invalid_token" -H "Content-Type: application/json" -w "\nHTTP状态码: %{http_code}"' \
        "401"
fi

# 5. 测试登出
echo "=== 5. 用户登出测试 ==="
run_test "用户登出" \
    'curl -s -X POST http://localhost:8080/api/auth/logout -H "Content-Type: application/json" -w "\nHTTP状态码: %{http_code}"' \
    "200"

# 6. 测试结果汇总
echo "=== 测试结果汇总 ==="
echo -e "总测试数: ${TOTAL_TESTS}"
echo -e "${GREEN}通过: ${PASSED_TESTS}${NC}"
echo -e "${RED}失败: ${FAILED_TESTS}${NC}"

if [ $FAILED_TESTS -eq 0 ]; then
    echo -e "${GREEN}🎉 所有测试通过！${NC}"
    exit 0
else
    echo -e "${RED}❌ 有测试失败，请检查API实现${NC}"
    exit 1
fi 