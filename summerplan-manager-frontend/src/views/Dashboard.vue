<template>
  <MainLayout>
    <template #default>
      <div class="dashboard-view">
        <h2>仪表盘（Dashboard）</h2>
        <el-row :gutter="24" style="margin-bottom: 24px;">
          <el-col :xs="24" :sm="12">
            <el-card>
              <template #header>
                <span>计划完成情况</span>
              </template>
              <div v-if="loadingPlans" style="text-align:center;padding:32px 0;">
                <el-icon><Loading /></el-icon>
              </div>
              <div v-else>
                <h3 class="chart-title">计划状态分布</h3>
                <div class="chart-container">
                  <div ref="planChartRef" style="height:420px;width:420px;margin:0 auto"></div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-card>
              <template #header>
                <span>近期任务完成情况</span>
              </template>
              <div v-if="loadingTasks" style="text-align:center;padding:32px 0;">
                <el-icon><Loading /></el-icon>
              </div>
              <div v-else>
                <div ref="chartRef" style="height:420px;width:420px;margin:0 auto"></div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </template>
  </MainLayout>
</template>

<script setup lang="ts">
import MainLayout from '@/layouts/MainLayout.vue';
import { ref, onMounted, nextTick, watch } from 'vue';
import { getPlans } from '@/api/plan';
import { getAllTasks } from '@/api/task';
import * as echarts from 'echarts';
import { ElMessage } from 'element-plus';
import 'element-plus/es/components/message/style/css';
import { Loading } from '@element-plus/icons-vue';

const loadingPlans = ref(true);
const loadingTasks = ref(true);
const activePlans = ref<any[]>([]);
const chartRef = ref<HTMLDivElement | null>(null);
let chartInstance: echarts.ECharts | null = null;

const planChartRef = ref<HTMLDivElement | null>(null);
let planChartInstance: echarts.ECharts | null = null;

// 统一option生成函数
const pieOption = (title: string, pieData: any[]) => ({
  title: { text: title, left: 'center', top: 10, textStyle: { fontSize: 18, fontWeight: 'bold' } },
  legend: { orient: 'vertical', top: 10, left: 10, textStyle: { fontSize: 14 }, itemGap: 16 },
  tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
  series: [{
    name: title,
    type: 'pie',
    radius: '70%',
    center: ['55%', '55%'],
    avoidLabelOverlap: false,
    data: pieData,
    emphasis: {
      itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.2)' }
    }
  }]
});

// 获取计划并统计各状态数量
const fetchPlans = async () => {
  loadingPlans.value = true;
  try {
    const plansResp = await getPlans();
    const planArr = Array.isArray(plansResp) ? plansResp : [];
    // 统计每种状态的数量
    const statusMap: Record<string, number> = {};
    planArr.forEach((p: any) => {
      const status = (p.status || '未知').trim();
      statusMap[status] = (statusMap[status] || 0) + 1;
    });
    const pieData = Object.entries(statusMap).map(([name, value]) => ({ name, value }));
    await nextTick();
    setTimeout(() => {
      if (planChartRef.value) {
        if (!planChartInstance) {
          planChartInstance = echarts.init(planChartRef.value);
        }
        planChartInstance.setOption({
          title: { show: false },
          legend: { orient: 'vertical', top: 10, left: 10, textStyle: { fontSize: 14 }, itemGap: 16 },
          tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
          series: [{
            name: '计划状态分布',
            type: 'pie',
            radius: '70%',
            center: ['55%', '55%'],
            avoidLabelOverlap: false,
            data: pieData,
            emphasis: {
              itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.2)' }
            }
          }]
        });
        planChartInstance.resize();
        window.dispatchEvent(new Event('resize'));
      }
    }, 100);
  } catch (e) {
    ElMessage.error('获取计划失败');
  } finally {
    loadingPlans.value = false;
  }
};

// 获取任务并统计各状态数量
const fetchTasks = async () => {
  loadingTasks.value = true;
  try {
    const raw = await getAllTasks();
    const tasks = Array.isArray(raw) ? raw : (raw && typeof raw === 'object' && 'data' in raw ? (raw as any).data : []);
    // 统计每种状态的数量
    const statusMap: Record<string, number> = {};
    tasks.forEach((t: any) => {
      const status = (t.status || '未知').trim();
      statusMap[status] = (statusMap[status] || 0) + 1;
    });
    const pieData = Object.entries(statusMap).map(([name, value]) => ({ name, value }));
    await nextTick();
    if (chartRef.value) {
      if (!chartInstance) {
        chartInstance = echarts.init(chartRef.value);
      }
      chartInstance.setOption(pieOption('任务状态分布', pieData));
      chartInstance.resize();
      // 保证自适应
      window.dispatchEvent(new Event('resize'));
    }
  } catch (e) {
    ElMessage.error('获取任务数据失败');
  } finally {
    loadingTasks.value = false;
  }
};

// ECharts初始化逻辑，监听loadingTasks变为false
watch(loadingTasks, async (val) => {
  if (val === false) {
    await nextTick();
    if (chartRef.value) {
      if (!chartInstance) {
        chartInstance = echarts.init(chartRef.value);
      }
      // 重新获取数据
      const raw = await getAllTasks();
      const tasks = Array.isArray(raw) ? raw : (raw && typeof raw === 'object' && 'data' in raw ? (raw as any).data : []);
      // 统计每种状态的数量
      const statusMap: Record<string, number> = {};
      tasks.forEach((t: any) => {
        const status = (t.status || '未知').trim();
        statusMap[status] = (statusMap[status] || 0) + 1;
      });
      const pieData = Object.entries(statusMap).map(([name, value]) => ({ name, value }));
      chartInstance.setOption({
        title: { text: '任务状态分布', left: 'center', top: 10, textStyle: { fontSize: 16 } },
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { orient: 'vertical', left: 'left' },
        series: [
          {
            name: '任务状态',
            type: 'pie',
            radius: '60%',
            data: pieData,
            emphasis: {
              itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.2)' }
            }
          }
        ]
      });
      chartInstance.resize();
    }
  }
});

onMounted(() => {
  fetchPlans();
  fetchTasks();
});
</script>

<style scoped>
.dashboard-view {
  padding: 32px 0;
}
.el-row {
  margin-bottom: 40px !important;
  justify-content: center;
}
.el-card__body {
  padding: 32px 16px 48px 16px !important;
}
.chart-title {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  margin: 0 0 20px 0;
  color: #303133;
}
.chart-container {
  display: flex;
  justify-content: center;
  align-items: center;
}
@media (max-width: 900px) {
  .dashboard-view {
    padding: 12px 0;
  }
  .el-row {
    flex-direction: column !important;
    gap: 24px;
    align-items: center;
  }
  .el-col {
    width: 100% !important;
    max-width: 100% !important;
  }
}
</style> 