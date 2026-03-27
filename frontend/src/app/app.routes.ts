import { Routes } from '@angular/router';
import { StudentPage } from './pages/student-page/student-page';
import { TeacherPage } from './pages/teacher-page/teacher-page';
import { CourseAdminPage } from './pages/course-admin-page/course-admin-page';
import { TechPage } from './pages/tech-page/tech-page';

export const routes: Routes = [
  { path: '', redirectTo: 'teacher', pathMatch: 'full' }, // escolha qual usuário simular
  { path: 'student', component: StudentPage },
  { path: 'teacher', component: TeacherPage },
  { path: 'course-admin', component: CourseAdminPage },
  { path: 'tech', component: TechPage },
  { path: '**', redirectTo: 'student' }
];