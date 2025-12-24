import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Post } from '../../models/post.model';
import { PostService } from '../../services/post.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-post-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './post-card.html'
})
export class PostCardComponent {

  @Input() post!: Post;
  @Output() deleted = new EventEmitter<void>();
  @Output() edit = new EventEmitter<Post>();

  constructor(
    private postService: PostService,
    public auth: AuthService
  ) {}

  onEdit(): void {
    this.edit.emit(this.post);
  }

  onDelete(): void {
    if (!confirm('¿Eliminar esta noticia?')) return;

    this.postService.delete(this.post.id).subscribe({
      next: () => this.deleted.emit(),
      error: () => alert('No tienes permisos para eliminar')
    });
  }
}


