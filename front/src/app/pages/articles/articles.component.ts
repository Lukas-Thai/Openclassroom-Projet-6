import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Article, ArticleService } from 'src/app/services/Articles/article.service';
import { Commentaire, CommentaireService } from 'src/app/services/Commentaires/commentaire.service';
import { ToastService } from 'src/app/services/SnackBar/snackbar.service';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.scss']
})
export class ArticlesComponent implements OnInit {
  private articleId!: number;
  article:Article | null=null;
  newComment: string = "";
  comments:Commentaire[] = [];
  constructor(private route:ActivatedRoute,private snackBar: ToastService,private router:Router, private artServ:ArticleService, private comServ:CommentaireService) { }

  ngOnInit(): void {
      this.route.paramMap.subscribe((params) => {
      const paramId :string | null = params.get('id');
      try{
        if(!paramId){//pas d'id
          throw new Error();
        }
        this.articleId = parseInt(paramId);
        this.artServ.getArticleById(this.articleId).subscribe({
          next: (data) => {
            this.article = data;
          },
          error: (err) => {
            console.error('Erreur lors du chargement de l\'article', err);
            this.snackBar.show("Article introuvable.",5000,'bottom');
            this.router.navigate(['/']);
          }
        });
        this.comServ.getAllCommentaires(this.articleId).subscribe({
          next: (data) => {
            this.comments = data;
          },
          error: (err) => {
            console.error('Erreur lors du chargement des commentaires', err);
          }
      });}
      catch(e){
        this.snackBar.show("Article introuvable.",5000,'bottom');
        this.router.navigate(['/']);
      }
    });
  }
sendComment(content: string) {
  if (!content.trim()) return; // évite les vides

  this.comServ.createCommentaire(this.articleId, content).subscribe({
    next: () => {
      this.snackBar.show("Commentaire envoyé !",5000,'bottom');
      this.newComment = ""; // reset textarea
      this.comServ.getAllCommentaires(this.articleId).subscribe(res => {
        this.comments = res; // refresh liste
      });
    },
    error: (err) => {
      console.error("Erreur envoi commentaire", err);
      this.snackBar.show("Échec de l'envoi du commentaire.",5000,'bottom');
    }
  });
}
}
