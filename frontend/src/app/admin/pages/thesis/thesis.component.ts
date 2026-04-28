import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { BaseComponent } from '@shared/directives/base-component';
import { EntityAttribute } from '@core/models/entity-attribute.model';
import { Thesis } from '@core/models/thesis.model';
import { getStudentDisplay } from '@core/models/student.model';
import { getTeacherDisplay } from '@core/models/teacher.model';
import { ThesisService } from '@core/services/thesis.service';
import { StudentService } from '@core/services/student.service';
import { TeacherService } from '@core/services/teacher.service';
import { Validators } from '@angular/forms';

@Component({
  selector: 'app-thesis',
  templateUrl: './thesis.component.html',
  styleUrls: ['./thesis.component.scss'],
})
export class ThesisComponent extends BaseComponent<Thesis> implements OnInit {
  title: string = 'Итоговые проекты';
  name: string = 'итоговый проект';
  attributes: EntityAttribute[] = [
    {
      key: 'id',
      name: 'ID',
      type: 'id',
    },
    {
      key: 'name',
      name: 'Название',
      type: 'string',
      required: true,
    },
    {
      key: 'description',
      name: 'Описание',
      type: 'lob',
    },
    {
      key: 'url',
      name: 'Ссылка',
      type: 'string',
      required: true,
    },
    {
      key: 'applicationDate',
      name: 'Дата подачи',
      type: 'date',
      required: true,
    },
    {
      key: 'defenseDate',
      name: 'Дата защиты',
      type: 'date',
    },
    {
      key: 'grade',
      name: 'Оценка',
      type: 'number',
      validators: [Validators.min(6), Validators.max(10)],
      errorMessage: 'Оценка должна быть от 6 до 10',
    },
    {
      key: 'student',
      name: 'Учащийся',
      type: 'select',
      required: true,
      display: getStudentDisplay,
    },
    {
      key: 'mentor',
      name: 'Руководитель проекта',
      type: 'select',
      required: true,
      display: getTeacherDisplay,
    },
  ];

  constructor(
    public override dialog: MatDialog,
    public override service: ThesisService,
    public studentService: StudentService,
    public teacherService: TeacherService
  ) {
    super();
  }

  ngOnInit(): void {
    this.getPage(this.tableData);
    this.getOptions('student', this.studentService);
    this.getOptions('mentor', this.teacherService);
  }

  override process(thesis: Thesis) {
    const studentId = thesis.student.id;
    if (!studentId) {
      return;
    }

    this.studentService.getThesisId(studentId).subscribe({
      next: (id) => {
        if (!thesis.id || (thesis.id && thesis.id !== id)) {
          window.alert(
            'Selected student already has thesis! Please try again!'
          );
          return;
        }
        super.process(thesis);
      },
      error: () => {
        super.process(thesis);
      },
    });
  }
}
