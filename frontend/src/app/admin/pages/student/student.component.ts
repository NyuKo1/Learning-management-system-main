import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EntityAttribute } from '@core/models/entity-attribute.model';
import { Student } from '@core/models/student.model';
import { getUserDisplay } from '@core/models/user.model';
import { getStudyProgramDisplay } from '@core/models/study-program.model';
import { StudentService } from '@core/services/student.service';
import { StudyProgramService } from '@core/services/study-program.service';
import { BaseUserComponent } from '../../directives/base-user-component';
import { UserService } from '@core/services/user.service';
import format from 'xml-formatter';
import { saveAs } from 'file-saver';
import { Validators } from '@angular/forms';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.scss'],
})
export class StudentComponent
  extends BaseUserComponent<Student>
  implements OnInit
{
  title: string = 'Учащиеся';
  name: string = 'учащийся';
  attributes: EntityAttribute[] = [
    {
      key: 'id',
      name: 'ID',
      type: 'id',
    },
    {
      key: 'user',
      name: 'Логин',
      type: 'text',
      required: true,
      display: getUserDisplay,
    },
    {
      key: 'password',
      name: 'Пароль',
      type: 'password',
      required: true,
      validators: [Validators.minLength(8)],
      errorMessage: 'Пароль должен содержать не менее 8 символов',
    },
    {
      key: 'firstName',
      name: 'Имя',
      type: 'text',
      required: true,
    },
    {
      key: 'lastName',
      name: 'Фамилия',
      type: 'text',
      required: true,
    },
    {
      key: 'index',
      name: 'Номер учащегося',
      type: 'text',
      required: true,
    },
    {
      key: 'yearOfEnrollment',
      name: 'Год зачисления',
      type: 'number',
      required: true,
      validators: [
        Validators.min(new Date().getFullYear() - 10),
        Validators.max(new Date().getFullYear()),
      ],
      errorMessage: `Год зачисления должен быть от ${
        new Date().getFullYear() - 10
      } до ${new Date().getFullYear()}`,
    },
    {
      key: 'studyProgram',
      name: 'Программа обучения',
      type: 'select',
      required: true,
      display: getStudyProgramDisplay,
    },
    {
      key: 'averageGrade',
      name: 'Средний балл',
      type: 'skip',
      sortable: false,
    },
    {
      key: 'totalECTS',
      name: 'Итого кредитов',
      type: 'skip',
      sortable: false,
    },
  ];

  constructor(
    public override dialog: MatDialog,
    public override service: StudentService,
    public override userService: UserService,
    public studyProgramService: StudyProgramService
  ) {
    super(userService);
  }

  ngOnInit(): void {
    this.getPage(this.tableData);
    this.getOptions('studyProgram', this.studyProgramService);
  }

  override process(value: any): void {
    if (value.id) {
      value.thesis = this.data.content.find((el) => el.id === value.id)?.thesis;
    }
    super.process(value);
  }

  exportPdf() {
    this.service.getAllPdf().subscribe((data) => {
      saveAs(data, 'students.pdf');
    });
  }

  exportXml() {
    this.service.getAllXml().subscribe((data) => {
      saveAs(new Blob([format(data)]), 'students.xml');
    });
  }
}
