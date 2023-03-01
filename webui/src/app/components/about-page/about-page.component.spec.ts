import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AboutPageComponent } from '@/app/components/about-page/about-page.component';
import { AppMaterialModule } from '@/app/app-material.module';

describe('AboutPageComponent', () => {
  let component: AboutPageComponent;
  let fixture: ComponentFixture<AboutPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AboutPageComponent],
      imports: [AppMaterialModule],
    })
      .compileComponents();

    fixture = TestBed.createComponent(AboutPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
