import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppMaterialModule } from '@/app/app-material.module';
import { BannerComponent } from '@/app/components/banner//banner.component';

describe('BannerComponent', () => {
  let component: BannerComponent;
  let fixture: ComponentFixture<BannerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BannerComponent],
      imports: [AppMaterialModule],
    })
      .compileComponents();

    fixture = TestBed.createComponent(BannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
