import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DebugElement } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Title } from '@angular/platform-browser';

import { AppMaterialModule } from '@/app/app-material.module';
import { ElectionPageComponent } from '@/app/components/election-page/election-page.component';

describe('CreateElectionComponent', () => {
  let component: ElectionPageComponent;
  let de: DebugElement;
  let fixture: ComponentFixture<ElectionPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ElectionPageComponent],
      imports: [
        AppMaterialModule,
        BrowserAnimationsModule,
        HttpClientTestingModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ElectionPageComponent);
    component = fixture.componentInstance;
    de = fixture.debugElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set the page title', () => {
    const titleService = TestBed.inject(Title);
    expect(titleService.getTitle()).toEqual('Create a new election - @gaber.dev');
  })
});
