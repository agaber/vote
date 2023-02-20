import { NgModule } from '@angular/core';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

const MATERIAL_COMPONENTS = [
  MatButtonModule,
  MatIconModule,
];

@NgModule({
  exports: MATERIAL_COMPONENTS,
  imports: MATERIAL_COMPONENTS,
})
export class MaterialModule { }
