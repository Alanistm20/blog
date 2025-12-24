import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

import { PostService, PostUpsertRequest } from '../../services/post.service';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category.model';
import { Post } from '../../models/post.model';
import { PostCreateRequest } from '../../models/post-create.model';

@Component({
  selector: 'app-post-form',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './post-form.html',
  styleUrls: ['./post-form.css']
})
export class PostFormComponent implements OnInit {
  categories: Category[] = [];
  id: number | null = null;

  // Usa null en vez de 0 (evita bugs con mat-select)
  form: {
    title: string;
    summary: string;
    content: string;
    categoryId: number | null;
    status: string;
  } = {
    title: '',
    summary: '',
    content: '',
    categoryId: null,
    status: 'DRAFT'
  };

  constructor(
    private categoryService: CategoryService,
    private postService: PostService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCategories();

    // Soporta distintas rutas: /posts/:id o /posts/:postId o /posts/:post_id
    const idParam =
      this.route.snapshot.paramMap.get('id') ??
      this.route.snapshot.paramMap.get('postId') ??
      this.route.snapshot.paramMap.get('post_id');

    this.id = idParam ? Number(idParam) : null;

    if (this.id && !Number.isNaN(this.id)) {
      this.postService.getOne(this.id).subscribe({
        next: (p: Post | any) => {
          // Compatibilidad: a veces viene categoryId, otras category.id
          const catId =
            Number(p?.categoryId ?? p?.category?.id ?? 0) || null;

          this.form = {
            title: p?.title ?? '',
            summary: p?.summary ?? '',
            content: p?.content ?? '',
            categoryId: catId,
            status: p?.status ?? 'DRAFT'
          };

          console.log('POST cargado =>', p);
          console.log('categoryId resuelto =>', this.form.categoryId);
        },
        error: (err) => {
          console.error('Error cargando noticia:', err);
          alert('No se pudo cargar la noticia');
        }
      });
    } else {
      this.id = null;
    }
  }

  loadCategories(): void {
    this.categoryService.list().subscribe({
      next: (res: Category[]) => {
        console.log('CATEGORIAS =>', res);

        // Si quieres mostrar solo activas, usa:
        // this.categories = res.filter(c => c.active);

        // Por ahora muestro todas (aunque active=false), y ordenadas por nombre:
        this.categories = [...res].sort((a, b) => a.name.localeCompare(b.name));
      },
      error: (err) => {
        console.error('Error cargando categorías:', err);
      }
    });
  }

  save(): void {
    if (!this.form.title?.trim()) {
      alert('Ingresa un título');
      return;
    }
    if (!this.form.summary?.trim()) {
      alert('Ingresa un resumen');
      return;
    }
    if (!this.form.content?.trim()) {
      alert('Ingresa el contenido');
      return;
    }

    const categoryIdNum = Number(this.form.categoryId);
    if (!categoryIdNum || categoryIdNum <= 0 || Number.isNaN(categoryIdNum)) {
      alert('Selecciona una categoría');
      return;
    }

    const payloadBase = {
      title: this.form.title.trim(),
      summary: this.form.summary.trim(),
      content: this.form.content.trim(),
      categoryId: categoryIdNum,
      status: (this.form.status ?? 'DRAFT') as any
    };

    if (this.id) {
      const updatePayload: PostUpsertRequest = payloadBase;

      this.postService.update(this.id, updatePayload).subscribe({
        next: () => this.router.navigate(['/']),
        error: (err) => {
          console.error('Error UPDATE:', err);
          const msg =
            typeof err?.error === 'string' ? err.error :
            err?.error?.message ? err.error.message :
            JSON.stringify(err?.error ?? err, null, 2);

          alert('Error al actualizar:\n' + msg);
        }
      });
    } else {
      const createPayload: PostCreateRequest = payloadBase as PostCreateRequest;

      console.log('CREATE payload =>', createPayload);

      this.postService.create(createPayload).subscribe({
        next: () => this.router.navigate(['/']),
        error: (err) => {
          console.error('Error CREATE:', err);
          const msg =
            typeof err?.error === 'string' ? err.error :
            err?.error?.message ? err.error.message :
            JSON.stringify(err?.error ?? err, null, 2);

          alert('Error al crear:\n' + msg);
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/']);
  }
}
