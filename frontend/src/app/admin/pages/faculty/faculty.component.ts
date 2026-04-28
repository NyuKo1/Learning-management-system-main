import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { BaseComponent } from '@shared/directives/base-component';
import { EntityAttribute } from '@core/models/entity-attribute.model';
import { Faculty } from '@core/models/faculty.model';
import { getTeacherDisplay } from '@core/models/teacher.model';
import { getAddressDisplay } from '@core/models/address.model';
import { FacultyService } from '@core/services/faculty.service';
import { AddressService } from '@core/services/address.service';
import { TeacherService } from '@core/services/teacher.service';

@Component({
  selector: 'app-faculty',
  templateUrl: './faculty.component.html',
  styleUrls: ['./faculty.component.scss'],
})
export class FacultyComponent extends BaseComponent<Faculty> implements OnInit {
  title: string = 'Направления';
  name: string = 'направление';
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
      key: 'description',
      name: 'Описание',
      type: 'lob',
      required: true,
    },
    {
      key: 'email',
      name: 'Email',
      type: 'email',
      required: true,
    },
    {
      key: 'dean',
      name: 'Руководитель',
      type: 'select',
      required: true,
      display: getTeacherDisplay,
    },
    {
      key: 'address',
      name: 'Адрес (необязательно)',
      type: 'select',
      required: false,
      display: getAddressDisplay,
    },
  ];

  constructor(
    public override dialog: MatDialog,
    public override service: FacultyService,
    public teacherService: TeacherService,
    public addressService: AddressService
  ) {
    super();
  }

  ngOnInit(): void {
    this.getPage(this.tableData);
    this.getOptions('dean', this.teacherService);
    this.getOptions('address', this.addressService);
  }
}
