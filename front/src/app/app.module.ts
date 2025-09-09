import { LOCALE_ID, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { InscriptionComponent } from './pages/inscription/inscription.component';
import { ConnexionComponent } from './pages/connexion/connexion.component';
import { HeaderComponent } from './components/header/header.component';

import { AppRoutingModule } from './app-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthInterceptor } from './services/Auth/auth.interceptor';
import { AccueilComponent } from './pages/accueil/accueil.component';
import { AuthGuard } from './services/Auth/auth.guard';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ThemesComponent } from './pages/themes/themes.component';
import { CreateArticleComponent } from './pages/create/article/article.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { NoAuthGuard } from './services/Auth/auth.noAuthGuard';
import { ArticlesComponent } from './pages/articles/articles.component';

@NgModule({
  declarations: [AppComponent, HomeComponent, InscriptionComponent, ConnexionComponent, HeaderComponent, AccueilComponent, ThemesComponent, CreateArticleComponent, ProfileComponent, ArticlesComponent],
  imports: [
    BrowserAnimationsModule, 
    MatSnackBarModule,
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
  ],
  providers: [ { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }, AuthGuard, NoAuthGuard],
  bootstrap: [AppComponent],
})
export class AppModule {}
