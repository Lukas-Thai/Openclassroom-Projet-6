import { Component, OnInit } from '@angular/core';
import { Article, ArticleService } from 'src/app/services/Articles/article.service';

@Component({
  selector: 'app-accueil',
  templateUrl: './accueil.component.html',
  styleUrls: ['./accueil.component.scss']
})
export class AccueilComponent implements OnInit {
  articles:Article[] = [];
   sortAsc: boolean = true;
  constructor(private artServ: ArticleService) { }

  ngOnInit(): void {
    this.artServ.getAllArticles().subscribe({
    next: (data) => {
      this.articles = data;
      console.log(this.articles);
    },
    error: (err) => {
      console.error('Erreur lors du chargement des articles', err);
    }
  });
  }
    toggleSort() {
    this.sortAsc = !this.sortAsc;
    this.sortArticles();
  }

  private sortArticles() {
    this.articles.sort((a, b) => {
      const dateA = new Date(a.created_at).getTime();
      const dateB = new Date(b.created_at).getTime();
      return this.sortAsc ? dateA - dateB : dateB - dateA;
    });
  }

}
