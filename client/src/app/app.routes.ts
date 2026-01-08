import { Component } from '@angular/core';
import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { TaskList } from './features/tasks/task-list/task-list';
import { redirectGuard } from './core/guards/redirect-guard';
import { authGuard } from './core/guards/auth-guard';

@Component({
    standalone: true,
    template: ''
})
export class EntryComponent{}

export const routes: Routes = [
    {
        path: '',
        component: EntryComponent,
        canActivate: [redirectGuard]
    },
    {
        path: 'login',
        component: Login
    },
    {
        path: 'register',
        component: Register
    },
    {
        path: 'tasks',
        component: TaskList,
        canActivate: [authGuard]
    }
];
