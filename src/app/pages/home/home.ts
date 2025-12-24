import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatChipsModule } from '@angular/material/chips';
import { MatButtonModule } from '@angular/material/button';

import { CategoryService } from '../../services/category.service';
import { PostService } from '../../services/post.service';
import { Category } from '../../models/category.model';
import { Post } from '../../models/post.model';
import { PostCardComponent } from '../../components/post-card/post-card';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatToolbarModule,
    MatChipsModule,
    MatButtonModule,
    PostCardComponent
  ],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class HomeComponent implements OnInit {
  categories: Category[] = [];
  posts: Post[] = [];
  selectedCategoryId = 0;

  constructor(
    private categoryService: CategoryService,
    private postService: PostService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCategories();
    this.loadPosts();
  }

  loadCategories(): void {
    this.categoryService.list().subscribe({
      next: (res: Category[]) => this.categories = res,
      error: (err) => console.error('ERROR /categories', err)
    });
  }

  loadPosts(): void {
    const cat = this.selectedCategoryId > 0 ? this.selectedCategoryId : undefined;
    this.postService.list(cat).subscribe({
      next: (res: Post[]) => this.posts = res,
      error: (err) => console.error('ERROR /posts', err)
    });
  }

  selectCategory(id: number): void {
    this.selectedCategoryId = id;
    this.loadPosts();
  }

  goNew(): void {
    this.router.navigate(['/posts/new']);
  }

  onEdit(post: Post): void {
    this.router.navigate(['/posts/edit', post.id]);
  }

  onDeleted(): void {
    this.loadPosts();
  }
}

