import request from '@/utils/request'
import type { Statistics, Department, ChartData, StatisticsFilter } from '@/types/statistics'

export const getStatistics = async (filter: StatisticsFilter): Promise<Statistics> => {
  const res = await request.post('/api/admin/statistics', filter)
  return res.data.stats
}

export const getDepartments = async (): Promise<Department[]> => {
  const res = await request.get('/api/departments')
  return res.data
}

export const getChartData = async (filter: StatisticsFilter): Promise<ChartData> => {
  const res = await request.post('/api/admin/statistics/charts', filter)
  return res.data
} 