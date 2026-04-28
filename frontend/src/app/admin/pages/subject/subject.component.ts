import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { BaseComponent } from '@shared/directives/base-component';
import { EntityAttribute } from '@core/models/entity-attribute.model';
import { Subject } from '@core/models/subject.model';
import { getStudyProgramDisplay } from '@core/models/study-program.model';
import { getTeacherDisplay } from '@core/models/teacher.model';
import { StudyProgramService } from '@core/services/study-program.service';
import { TeacherService } from '@core/services/teacher.service';
import { SubjectService } from '@core/services/subject.service';
import { CrmService } from '@core/services/crm.service';
import { Validators } from '@angular/forms';

@Component({
  selector: 'app-subject',
  templateUrl: './subject.component.html',
  styleUrls: ['./subject.component.scss'],
})
export class SubjectComponent extends BaseComponent<Subject> implements OnInit {
  title: string = 'Subjects';
  name: string = 'subject';
  attributes: EntityAttribute[] = [
    { key: 'id', name: 'ID', type: 'id' },
    { key: 'name', name: 'Name', type: 'text', required: true },
    { key: 'syllabus', name: 'Syllabus / Description', type: 'lob', required: true },
    {
      key: 'semester', name: 'Module (1–8)', type: 'number', required: true,
      validators: [Validators.min(1), Validators.max(8)],
      errorMessage: 'Module number must be between 1 and 8',
    },
    {
      key: 'ects', name: 'ECTS Credits (1–10)', type: 'number', required: true,
      validators: [Validators.min(1), Validators.max(10)],
      errorMessage: 'ECTS must be between 1 and 10',
    },
    { key: 'studyProgram', name: 'Study Program', type: 'select', required: true, display: getStudyProgramDisplay },
    { key: 'professor', name: 'Professor', type: 'select', required: true, display: getTeacherDisplay },
    { key: 'assistant', name: 'Teaching Assistant', type: 'select', required: true, display: getTeacherDisplay },
  ];

  constructor(
    public override dialog: MatDialog,
    public override service: SubjectService,
    public studyProgramService: StudyProgramService,
    public teacherService: TeacherService,
    private crmService: CrmService
  ) {
    super();
  }

  ngOnInit(): void {
    this.getPage(this.tableData);
    this.getOptions('studyProgram', this.studyProgramService);
    this.getOptions('professor', this.teacherService);
    this.getOptions('assistant', this.teacherService);
  }

  override process(value: Subject): void {
    delete (value as any)['ids'];
    if (value.id) {
      this.service.update(value.id, value).subscribe({
        next: () => this.getPage(),
        error: () => window.alert('Something went wrong! Please try again'),
      });
    } else {
      this.service.create(value).subscribe({
        next: (saved) => {
          this.getPage();
          this.crmService.createCourseFromSubject(saved).subscribe({
            error: (err) => console.warn('CRM course sync failed:', err),
          });
        },
        error: () => window.alert('Something went wrong! Please try again'),
      });
    }
  }
}
