import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { BaseComponent } from '@shared/directives/base-component';
import { EntityAttribute } from '@core/models/entity-attribute.model';
import { StudyProgram } from '@core/models/study-program.model';
import { getFacultyDisplay } from '@core/models/faculty.model';
import { getTeacherDisplay } from '@core/models/teacher.model';
import { StudyProgramService } from '@core/services/study-program.service';
import { FacultyService } from '@core/services/faculty.service';
import { TeacherService } from '@core/services/teacher.service';
import { CrmService } from '@core/services/crm.service';
import { Validators } from '@angular/forms';

@Component({
  selector: 'app-study-program',
  templateUrl: './study-program.component.html',
  styleUrls: ['./study-program.component.scss'],
})
export class StudyProgramComponent
  extends BaseComponent<StudyProgram>
  implements OnInit
{
  title: string = 'Study Programs';
  name: string = 'study program';
  attributes: EntityAttribute[] = [
    { key: 'id', name: 'ID', type: 'id' },
    { key: 'name', name: 'Name', type: 'text', required: true },
    { key: 'description', name: 'Description', type: 'lob', required: true },
    {
      key: 'acronym', name: 'Acronym', type: 'text', required: true,
      validators: [Validators.maxLength(5)],
      errorMessage: 'Acronym must be at most 5 characters',
    },
    { key: 'faculty', name: 'Faculty', type: 'select', required: true, display: getFacultyDisplay },
    { key: 'manager', name: 'Program Manager', type: 'select', required: true, display: getTeacherDisplay },
  ];

  constructor(
    public override dialog: MatDialog,
    public override service: StudyProgramService,
    public facultyService: FacultyService,
    public teacherService: TeacherService,
    private crmService: CrmService,
  ) {
    super();
  }

  ngOnInit(): void {
    this.getPage(this.tableData);
    this.getOptions('faculty', this.facultyService);
    this.getOptions('manager', this.teacherService);
  }

  override process(value: StudyProgram) {
    delete (value as any)['ids'];
    const isNew = !value.id;

    const operation = value.id
      ? this.service.update(value.id, value)
      : this.service.create(value);

    operation.subscribe({
      next: (saved: StudyProgram) => {
        this.getPage();
        if (isNew && saved?.id) {
          this.createCrmCourse(saved);
        }
      },
      error: () => {
        window.alert('Something went wrong saving the study program. Please try again.');
      },
    });
  }

  private createCrmCourse(program: StudyProgram): void {
    const course = {
      title: program.name,
      description: program.description || `Study program: ${program.name}`,
      instructor: 'Faculty Staff',
      category: 'Academic Program',
      level: 'BEGINNER',
      duration: '1 year',
      lessons: 0,
      price: 0,
      rating: 0,
      studentsCount: 0,
      tags: program.name,
      color: '#6366f1',
      icon: 'school',
      available: false,
      studyProgramId: program.id,
    };

    this.crmService.createCourse(course).subscribe({
      next: () => {},
      error: () => {},
    });
  }
}
