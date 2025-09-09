import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ArticleService } from 'src/app/services/Articles/article.service';
import { ToastService } from 'src/app/services/SnackBar/snackbar.service';
import { Theme, ThemeService } from 'src/app/services/Themes/theme.service';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class CreateArticleComponent implements OnInit {
  themes:Theme[] = [];
  selectedTheme: Theme | null = null;
  title: string = "";
  content: string = "";
  constructor(private themeServ:ThemeService,private route:Router, private artServ:ArticleService,private snackBar: ToastService) { }

  ngOnInit(): void {
    this.themeServ.getAllThemes().subscribe({
      next: (data) => {
        this.themes = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des thèmes', err);
      }
    });
  }
  createArticle(theme:Theme|null,title: string, content: string) {
    if(!theme || !title || !content){
      alert("Veuillez remplir tous les champs.");
      return;
    }
    this.artServ.createArticle(theme.idTheme,title,content).subscribe({
      next: () => {
        this.snackBar.show("Article créé avec succès !",5000,'bottom');
        this.route.navigate(['']);
      },
      error: (err) => {
        console.error('Erreur lors de la création de l\'article', err);
        this.snackBar.show("Erreur lors de la création de l'article.",5000,'bottom');
      }
    });
  }
}
