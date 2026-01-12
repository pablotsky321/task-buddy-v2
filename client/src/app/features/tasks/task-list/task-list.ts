import { Component, inject, isDevMode, OnInit } from '@angular/core';
import { Task } from '../../../core/types/task.type';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TaskService } from '../task.service';
import { finalize } from 'rxjs';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-task-list',
  imports: [ReactiveFormsModule],
  templateUrl: './task-list.html',
  styleUrl: './task-list.css',
})
export class TaskList implements OnInit {

  //Services
  taskService = inject(TaskService)
  authService = inject(AuthService)

  //Data
  task_list: Task[] = [];

  //ControlUI
  loadingPage = false;
  loadingReq = false;
  show_update_form: { task: Task | null, show: boolean } = { task: null, show: false }
  show_details: { task: Task | null, show: boolean } = { task: null, show: false }

  loadData() {
    this.loadingPage = true;
    this.taskService.getTasks()
      .pipe(finalize(() => this.loadingPage = false))
      .subscribe({
        next: (res) => {
          this.task_list = res;
        },
        error: (err) => {
          if (isDevMode()) console.error(err);
          const error = err.error;
          alert(typeof (error) === 'string' ? error : "Something went wrong")
        }
      })
  }

  ngOnInit(): void {
    this.loadData();
  }

  //form
  task_form = new FormGroup({
    title: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required]),
    finishAt: new FormControl('', [Validators.required])
  })

  addTask() {
    this.loadingReq = true;
    this.taskService.createTask(this.formFormatted())
      .pipe(finalize(() => this.loadingReq = false))
      .subscribe({
        next: (res) => {
          this.loadData();
          this.cleanForm();
        },
        error: (err) => {
          if (isDevMode()) console.error(err);
          const error = err.error;
          alert(typeof (error) === 'string' ? error : "Something went wrong")
        }
      })
  }

  formFormatted() {
    const taskValue = this.task_form.value;
    taskValue.finishAt = new Date(taskValue.finishAt as string).toISOString();
    return taskValue as Task;
  }

  cleanForm() {
    this.task_form.patchValue({
      title: '',
      description: '',
      finishAt: ''
    })
  }

  delete(id: string) {
    this.loadingReq = true;
    this.taskService.deleteTask(id)
      .pipe(finalize(() => this.loadingReq = false))
      .subscribe({
        next: (res) => {
          alert(res)
          this.loadData();
        },
        error: (err) => {
          if (isDevMode()) console.error(err);
          const error = err.error;
          alert(typeof (error) === 'string' ? error : "Something went wrong")
        }
      })
  }

  openShowDetails(id: string) {
    this.loadingReq = true;
    this.taskService.getTaskDetails(id)
      .pipe(finalize(() => this.loadingReq = false))
      .subscribe({
        next: (res) => {
          this.show_details.task = res;
          this.show_details.show = true;
        },
        error: (err) => {
          if (isDevMode()) console.error(err);
          const error = err.error;
          alert(typeof (error) === 'string' ? error : "Something went wrong")
        }
      })
  }

  closeShowDetails() {
    this.show_details.show = false;
    this.show_details.task = null;
  }

  openEditForm(id: string) {
    this.loadingReq = true;
    this.taskService.getTaskDetails(id)
      .pipe(finalize(() => this.loadingReq = false))
      .subscribe({
        next: (res) => {
          this.show_update_form.task = res;
          this.show_update_form.show = true;
          this.task_form.patchValue({
            title: res.title,
            description: res.description,
            finishAt: this.formatDateToInput(res.finishAt)
          })
        },
        error: (err) => {
          if (isDevMode()) console.error(err);
          const error = err.error;
          alert(typeof (error) === 'string' ? error : "Something went wrong")
        }
      })
  }

  closeEditForm() {
    this.show_update_form.show = false;
    this.show_update_form.task = null;
    this.cleanForm();
  }

  updateTask() {
    this.loadingReq = true;
    this.taskService.updateTask(this.formFormatted(), this.show_update_form.task?.id as string)
      .pipe(finalize(() => this.loadingReq = false))
      .subscribe({
        next: (res) => {
          this.loadData();
          this.closeEditForm();
        },
        error: (err) => {
          if (isDevMode()) console.error(err);
          const error = err.error;
          alert(typeof (error) === 'string' ? error : "Something went wrong")
        }
      })
  }

  logout() {
    this.authService.logout()
  }

  completeTask(id: string) {
    this.loadingReq = true;
    this.taskService.completeTask(id)
      .pipe(finalize(() => this.loadingReq = false))
      .subscribe({
        next: (res) => {
          this.loadData();
        },
        error: (err) => {
          if (isDevMode()) console.error(err);
          const error = err.error;
          alert(typeof (error) === 'string' ? error : "Something went wrong")
        }
      })
  }

  //data formatter
  formatDateToInput(dateString: string): string {
    const date = new Date(dateString);

    const pad = (n: number) => n.toString().padStart(2, '0');

    const year = date.getFullYear();
    const month = pad(date.getMonth() + 1);
    const day = pad(date.getDate());
    const hours = pad(date.getHours());
    const minutes = pad(date.getMinutes());

    return `${year}-${month}-${day}T${hours}:${minutes}`;
  }


}
