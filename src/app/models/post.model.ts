export interface Post {
  id: number;
  title: string;
  summary: string;
  content: string;
  status: string;
  publishedAt?: string | null;

  categoryId: number;       
  categoryName: string;

  authorName?: string | null;
}
