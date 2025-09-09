import { Component, OnInit } from '@angular/core';
import { AbonnementService } from 'src/app/services/Abonnements/abonnement.service';
import { ToastService } from 'src/app/services/SnackBar/snackbar.service';
import { Theme, ThemeService } from 'src/app/services/Themes/theme.service';

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss']
})
export class ThemesComponent implements OnInit {
  themes:Theme[] = [];
  abonnements:number[] = [];
  constructor(private snackbar:ToastService, private themeServ:ThemeService, private abonServ:AbonnementService) { }

  ngOnInit(): void {
    this.themeServ.getAllThemes().subscribe({
      next: (data) => {
        this.themes = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des thèmes', err);
        this.snackbar.show("Erreur lors du chargement des thèmes",5000,'bottom');
      }
    });
    this.abonServ.getAbonnements().subscribe({
      next: (data) => {
        this.abonnements = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des abonnements', err);
        this.snackbar.show("Erreur lors du chargement des abonnements",5000,'bottom');
      }
    });
  }
  subscribe(theme:Theme){
    this.themeServ.subscribeOrUnsubscribe(theme.idTheme).subscribe({
      next: () => {
        this.abonnements.push(theme.idTheme);
        this.snackbar.show(`Abonné au thème ${theme.name}`,5000,'bottom');
      },
      error: (err) => {
        console.error('Erreur lors de l\'abonnement', err);
        this.snackbar.show("Erreur lors de l'abonnement",5000,'bottom');
      }
    });
  }
}
