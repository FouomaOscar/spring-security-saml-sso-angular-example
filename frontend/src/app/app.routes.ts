import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { HomeComponent } from './home/home.component';
import { AuthComponent } from './auth/auth.component';
import { securityInnerGuard } from './core/guards/security-inner.guard';

export const routes: Routes = [
    {
        path: 'auth',
        component: AuthComponent,
        canActivate: [securityInnerGuard]
    },
    {
        path: 'home',
        component: HomeComponent,
        canActivate: [authGuard]
    },
    { path: '**', redirectTo: 'home', pathMatch: 'full' }
];
