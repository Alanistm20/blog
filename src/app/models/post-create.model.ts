export interface PostCreateRequest {
  title: string;
  summary: string;
  content: string;
  categoryId: number;
  status?: string; // 'DRAFT' | 'PUBLISHED'
}
