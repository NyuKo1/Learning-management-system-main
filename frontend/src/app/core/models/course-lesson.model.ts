export interface CourseLesson {
  id?: number;
  courseId: number;
  title: string;
  description?: string;
  videoUrl?: string;
  content?: string;
  orderIndex: number;
  duration?: string;
}
