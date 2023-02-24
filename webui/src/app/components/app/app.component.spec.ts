import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { TestBed } from '@angular/core/testing';

import { AppComponent } from '@/app/components/app/app.component';
import { AppMaterialModule } from '@/app/app-material.module';
import { CreateElectionComponent } from '@/app/components/election-page/election-page.component';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        AppMaterialModule,
        HttpClientTestingModule,
        RouterTestingModule,
      ],
      declarations: [
        AppComponent,
        CreateElectionComponent,
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });
});
