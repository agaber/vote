import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { AppMaterialModule } from '@/app/app-material.module';
import { ElectionPageComponent } from '@/app/components/election-page/election-page.component';

describe('CreateElectionComponent', () => {
  let component: ElectionPageComponent;
  let fixture: ComponentFixture<ElectionPageComponent>;


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ElectionPageComponent],
      imports: [
        AppMaterialModule,
        BrowserAnimationsModule,
        HttpClientTestingModule,
        ReactiveFormsModule,],
    }).compileComponents();

    fixture = TestBed.createComponent(ElectionPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
