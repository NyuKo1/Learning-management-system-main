export interface CourseCatalog {
  id?: number;
  title: string;
  description: string;
  instructor: string;
  category: string;
  level: string;
  duration: string;
  lessons: number;
  price: number;
  originalPrice?: number;
  rating: number;
  studentsCount: number;
  tags: string;
  color: string;
  icon: string;
  available?: boolean;
  purchased?: boolean;
  subjectId?: number;
  studyProgramId?: number;
}
