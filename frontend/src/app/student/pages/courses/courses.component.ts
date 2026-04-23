import { Component, OnInit } from '@angular/core';

export interface Course {
  id: number;
  title: string;
  description: string;
  instructor: string;
  category: string;
  level: 'Beginner' | 'Intermediate' | 'Advanced';
  duration: string;
  lessons: number;
  price: number;
  originalPrice?: number;
  rating: number;
  students: number;
  tags: string[];
  color: string;
  icon: string;
  enrolled?: boolean;
}

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.scss'],
})
export class CoursesComponent implements OnInit {
  searchQuery: string = '';
  selectedCategory: string = 'All';
  selectedLevel: string = 'All';
  purchaseInProgress: number | null = null;
  purchaseSuccess: number | null = null;

  categories = ['All', 'Programming', 'Mathematics', 'Science', 'Business', 'Design', 'Languages'];
  levels = ['All', 'Beginner', 'Intermediate', 'Advanced'];

  allCourses: Course[] = [
    {
      id: 1,
      title: 'Advanced Algorithms & Data Structures',
      description: 'Master complex algorithms, graph theory, dynamic programming, and build efficient solutions for real-world engineering challenges.',
      instructor: 'Dr. Elena Kovacs',
      category: 'Programming',
      level: 'Advanced',
      duration: '48h',
      lessons: 120,
      price: 89,
      originalPrice: 149,
      rating: 4.9,
      students: 3420,
      tags: ['Algorithms', 'Python', 'C++'],
      color: '#6366f1',
      icon: 'code',
      enrolled: false,
    },
    {
      id: 2,
      title: 'Calculus & Linear Algebra Mastery',
      description: 'A comprehensive journey through differential equations, matrix operations, and vector spaces designed for engineering and CS students.',
      instructor: 'Prof. Marcus Chen',
      category: 'Mathematics',
      level: 'Intermediate',
      duration: '36h',
      lessons: 95,
      price: 69,
      originalPrice: 119,
      rating: 4.8,
      students: 5120,
      tags: ['Calculus', 'Linear Algebra', 'Statistics'],
      color: '#0ea5e9',
      icon: 'functions',
      enrolled: false,
    },
    {
      id: 3,
      title: 'Full-Stack Web Development Bootcamp',
      description: 'Build modern web applications from scratch using Angular, Node.js, and PostgreSQL. Includes real-world project portfolio.',
      instructor: 'Sarah Mitchell',
      category: 'Programming',
      level: 'Beginner',
      duration: '60h',
      lessons: 180,
      price: 99,
      originalPrice: 199,
      rating: 4.7,
      students: 8900,
      tags: ['Angular', 'Node.js', 'SQL'],
      color: '#10b981',
      icon: 'web',
      enrolled: true,
    },
    {
      id: 4,
      title: 'Quantum Physics: From Theory to Application',
      description: 'Explore quantum mechanics, wave-particle duality, and the mathematics behind modern quantum computing concepts.',
      instructor: 'Dr. Yuki Tanaka',
      category: 'Science',
      level: 'Advanced',
      duration: '42h',
      lessons: 105,
      price: 79,
      rating: 4.9,
      students: 1830,
      tags: ['Quantum', 'Physics', 'Math'],
      color: '#8b5cf6',
      icon: 'science',
      enrolled: false,
    },
    {
      id: 5,
      title: 'Business Analytics & Data-Driven Strategy',
      description: 'Learn how to extract insights from business data, build dashboards, and make evidence-based decisions that drive growth.',
      instructor: 'Dr. Amara Diallo',
      category: 'Business',
      level: 'Intermediate',
      duration: '30h',
      lessons: 78,
      price: 59,
      originalPrice: 99,
      rating: 4.6,
      students: 6700,
      tags: ['Analytics', 'Excel', 'PowerBI'],
      color: '#f59e0b',
      icon: 'bar_chart',
      enrolled: false,
    },
    {
      id: 6,
      title: 'UI/UX Design Fundamentals',
      description: 'Design beautiful, user-centric interfaces using Figma. Learn the principles of visual hierarchy, accessibility, and prototyping.',
      instructor: 'Luna Park',
      category: 'Design',
      level: 'Beginner',
      duration: '24h',
      lessons: 65,
      price: 49,
      originalPrice: 89,
      rating: 4.8,
      students: 11200,
      tags: ['Figma', 'UX', 'Prototyping'],
      color: '#ec4899',
      icon: 'palette',
      enrolled: false,
    },
    {
      id: 7,
      title: 'Machine Learning Engineering',
      description: 'Deploy production-ready ML models using PyTorch, FastAPI, and cloud infrastructure. Covers MLOps, monitoring, and scalability.',
      instructor: 'Dr. Ravi Sharma',
      category: 'Programming',
      level: 'Advanced',
      duration: '55h',
      lessons: 140,
      price: 119,
      originalPrice: 199,
      rating: 4.9,
      students: 4350,
      tags: ['ML', 'Python', 'PyTorch'],
      color: '#14b8a6',
      icon: 'psychology',
      enrolled: false,
    },
    {
      id: 8,
      title: 'Academic Spanish for Professionals',
      description: 'Go beyond basics — master professional written and spoken Spanish for academic research, conferences, and international collaboration.',
      instructor: 'Isabella Moreno',
      category: 'Languages',
      level: 'Intermediate',
      duration: '28h',
      lessons: 72,
      price: 45,
      originalPrice: 79,
      rating: 4.7,
      students: 3100,
      tags: ['Spanish', 'Academic', 'Writing'],
      color: '#f97316',
      icon: 'translate',
      enrolled: false,
    },
  ];

  get filteredCourses(): Course[] {
    return this.allCourses.filter(course => {
      const matchesSearch =
        !this.searchQuery ||
        course.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        course.description.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        course.tags.some(t => t.toLowerCase().includes(this.searchQuery.toLowerCase()));
      const matchesCategory = this.selectedCategory === 'All' || course.category === this.selectedCategory;
      const matchesLevel = this.selectedLevel === 'All' || course.level === this.selectedLevel;
      return matchesSearch && matchesCategory && matchesLevel;
    });
  }

  get enrolledCourses(): Course[] {
    return this.allCourses.filter(c => c.enrolled);
  }

  constructor() {}

  ngOnInit(): void {}

  selectCategory(cat: string): void {
    this.selectedCategory = cat;
  }

  selectLevel(level: string): void {
    this.selectedLevel = level;
  }

  getLevelColor(level: string): string {
    const map: Record<string, string> = {
      Beginner: '#10b981',
      Intermediate: '#f59e0b',
      Advanced: '#ef4444',
    };
    return map[level] || '#6b7280';
  }

  formatStudents(count: number): string {
    if (count >= 1000) return (count / 1000).toFixed(1) + 'k';
    return count.toString();
  }

  formatPrice(price: number): string {
    return '$' + price.toFixed(0);
  }

  getDiscount(price: number, original: number): number {
    return Math.round(((original - price) / original) * 100);
  }

  renderStars(rating: number): string[] {
    const stars: string[] = [];
    for (let i = 1; i <= 5; i++) {
      if (rating >= i) stars.push('star');
      else if (rating >= i - 0.5) stars.push('star_half');
      else stars.push('star_border');
    }
    return stars;
  }

  purchaseCourse(course: Course, event: Event): void {
    event.stopPropagation();
    if (course.enrolled || this.purchaseInProgress !== null) return;

    this.purchaseInProgress = course.id;
    // Simulate payment processing
    setTimeout(() => {
      course.enrolled = true;
      course.students += 1;
      this.purchaseInProgress = null;
      this.purchaseSuccess = course.id;
      setTimeout(() => (this.purchaseSuccess = null), 3000);
    }, 1800);
  }
}
