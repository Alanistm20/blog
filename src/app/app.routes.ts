import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home';
import { LoginComponent } from './pages/login/login';
import { PostFormComponent } from './pages/post-form/post-form';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'posts/new', component: PostFormComponent, canActivate: [authGuard] },
  { path: 'posts/edit/:id', component: PostFormComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '' }
];
