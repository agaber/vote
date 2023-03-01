import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DebugElement } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { AppMaterialModule } from '@/app/app-material.module';
import { HelpIconButtonComponent } from '@/app/components/help-icon-button/help-icon-button.component';
import { VotePageComponent } from '@/app/components/vote-page/vote-page.component';

describe('VotePageComponent', () => {
  let component: VotePageComponent;
  let de: DebugElement;
  let fixture: ComponentFixture<VotePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        HelpIconButtonComponent,
        VotePageComponent,
      ],
      imports: [
        AppMaterialModule,
        BrowserAnimationsModule,
        HttpClientTestingModule,
        RouterTestingModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(VotePageComponent);
    component = fixture.componentInstance;
    de = fixture.debugElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
