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
import { Validators } from '@angular/forms';

@Component({
  selector: 'app-subject',
  templateUrl: './subject.component.html',
  styleUrls: ['./subject.component.scss'],
})
export class SubjectComponent extends BaseComponent<Subject> implements OnInit {
  title: string = 'Дисциплины';
  name: string = 'дисциплина';
  attributes: EntityAttribute[] = [
    {
      key: 'id',
      name: 'ID',
      type: 'id',
    },
    {
      key: 'name',
      name: 'Название',
      type: 'text',
      required: true,
    },
    {
      key: 'syllabus',
      name: 'Описание программы',
      type: 'lob',
      required: true,
    },
    {
      key: 'semester',
      name: 'Модуль (1–8)',
      type: 'number',
      required: true,
      validators: [Validators.min(1), Validators.max(8)],
      errorMessage: 'Введите номер модуля от 1 до 8',
    },
    {
      key: 'ects',
      name: 'Уровень (1–5)',
      type: 'number',
      required: true,
      validators: [Validators.min(1), Validators.max(5)],
      errorMessage: 'Введите уровень от 1 до 5',
    },
    {
      key: 'studyProgram',
      name: 'Программа обучения',
      type: 'select',
      required: true,
      display: getStudyProgramDisplay,
    },
    {
      key: 'professor',
      name: 'Преподаватель',
      type: 'select',
      required: true,
      display: getTeacherDisplay,
    },
    {
      key: 'assistant',
      name: 'Ассистент',
      type: 'select',
      required: true,
      display: getTeacherDisplay,
    },
  ];

  constructor(
    public override dialog: MatDialog,
    public override service: SubjectService,
    public studyProgramService: StudyProgramService,
    public teacherService: TeacherService
  ) {
    super();
  }

  ngOnInit(): void {
    this.getPage(this.tableData);
    this.getOptions('studyProgram', this.studyProgramService);
    this.getOptions('professor', this.teacherService);
    this.getOptions('assistant', this.teacherService);
  }
}
