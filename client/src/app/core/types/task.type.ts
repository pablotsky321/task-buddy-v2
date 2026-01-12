export interface Task {
  id: string
  title: string
  description: string
  createdAt: string
  updatedAt: string
  finishAt: string
  completedAt: string
  state: 'finished' | 'completed' | 'expired' | 'pending'
}
