import { Component, OnInit } from '@angular/core';
import { CrmApiService } from '../../../core/services/crm-api.service';
import { CrmCourse } from '../../../core/models/course.model';

@Component({
  selector: 'crm-courses-list',
  templateUrl: './courses-list.component.html',
  styleUrls: ['./courses-list.component.scss']
})
export class CoursesListComponent implements OnInit {
  courses: CrmCourse[] = [];
  loading = true;
  loadError = false;

  constructor(private api: CrmApiService) {}

  ngOnInit(): void {
    this.loadError = false;
    this.loading = true;
    this.api.getCourses().subscribe({
      next: data => { this.courses = data; this.loading = false; },
      error: () => { this.loadError = true; this.courses = []; this.loading = false; }
    });
  }
}
