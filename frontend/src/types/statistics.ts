export interface Statistics {
  totalStudents: number
  totalCourses: number
  averageScore: number
}

export interface Department {
  id: number
  name: string
  averageScore?: number
}

export interface ChartData {
  scoreDistribution: number[]
  departmentComparison: number[]
  courseTrend: number[]
}

export interface StatisticsFilter {
  timeRange: 'semester' | 'year' | 'all'
  department?: string
} 