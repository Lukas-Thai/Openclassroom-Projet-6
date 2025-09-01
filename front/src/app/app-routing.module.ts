import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { InscriptionComponent } from './pages/inscription/inscription.component';
import { ConnexionComponent } from './pages/connexion/connexion.component';
import { AccueilComponent } from './pages/accueil/accueil.component';
import { AuthGuard } from './services/Auth/auth.guard';
import { NoAuthGuard } from './services/Auth/auth.noAuthGuard';
import { ThemesComponent } from './pages/themes/themes.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { CreateArticleComponent } from './pages/create/article/article.component';
import { CreateThemeComponent } from './pages/create/theme/theme.component';


const routes: Routes = [{ path: '', component: HomeComponent, canActivate: [NoAuthGuard] },
{path:'inscription', component: InscriptionComponent, canActivate: [NoAuthGuard]},
{path:'connexion',component: ConnexionComponent, canActivate: [NoAuthGuard]},
{path:'accueil',component: AccueilComponent, canActivate: [AuthGuard]},
{path:'theme', component:ThemesComponent,canActivate: [AuthGuard]},
{path:'profile', component:ProfileComponent,canActivate: [AuthGuard]},
{path:'createArticle',component:CreateArticleComponent,canActivate: [AuthGuard]},
{path:'createTheme',component:CreateThemeComponent,canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
