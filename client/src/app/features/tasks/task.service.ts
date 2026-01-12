import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Task } from '../../core/types/task.type';

@Injectable({
  providedIn: 'root',
})
export class TaskService {

  private readonly API_URL = `${environment.API_URL}/api/tasks`;
  private readonly httpReq = inject(HttpClient);

  public getTasks() {
    return this.httpReq.get<Task[]>(`${this.API_URL}/mytasks`);
  }

  public getTaskDetails(taskId: string) {
    return this.httpReq.get<Task>(`${this.API_URL}/details/${taskId}`);
  }

  public createTask(task: Task) {
    return this.httpReq.post<Task>(`${this.API_URL}/create`, task);
  }

  public updateTask(task: Task, id: string) {
    return this.httpReq.put<Task>(`${this.API_URL}/update/${id}`, task);
  }

  public deleteTask(id: string) {
    return this.httpReq.delete<string>(`${this.API_URL}/delete/${id}`, { responseType: 'text' as 'json' });
  }

  public completeTask(id: string) {
    return this.httpReq.put<string>(`${this.API_URL}/complete/${id}`, {}, { responseType: 'text' as 'json' });
  }

}
