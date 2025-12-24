import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../models/post.model';

export interface PostUpsertRequest {
  title: string;
  summary: string;
  content: string;
  categoryId: number;
  status?: string; // 'DRAFT' | 'PUBLISHED'
}

@Injectable({ providedIn: 'root' })
export class PostService {
  private api = 'http://localhost:8080/posts';

  constructor(private http: HttpClient) {}

  list(categoryId?: number): Observable<Post[]> {
    let params = new HttpParams();
    if (categoryId && categoryId > 0) {
      params = params.set('categoryId', categoryId);
    }
    return this.http.get<Post[]>(this.api, { params });
  }

  getOne(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.api}/${id}`);
  }

  create(body: PostUpsertRequest): Observable<Post> {
    return this.http.post<Post>(this.api, body);
  }

  update(id: number, body: PostUpsertRequest): Observable<Post> {
    return this.http.put<Post>(`${this.api}/${id}`, body);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}
