import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { AbonnementService } from 'src/app/services/Abonnements/abonnement.service';
import { ToastService } from 'src/app/services/SnackBar/snackbar.service';
import { Theme, ThemeService } from 'src/app/services/Themes/theme.service';
import { UserService } from 'src/app/services/User/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  themes:Theme[] = [];
  abonnements:number[] = [];
  profileForm!: FormGroup;
  
  constructor(private fb: FormBuilder,private snackbar:ToastService, private themeServ:ThemeService, private abonServ:AbonnementService, private userServ:UserService) { }

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      username: [''],
      email: ['', Validators.email],
      password: ['', this.passwordValidator] // ici on met notre validator custom
    });
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
  get subscribedThemes(): Theme[] {
  return this.themes.filter(t => this.abonnements.includes(t.idTheme));
}
  get password() {
    return this.profileForm.get('password');
  }
  get atLeastOneFieldFilled(): boolean {
    const { username, email, password } = this.profileForm.value;
    return !!(username || email || password);
  }
  passwordValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value;
  if (!value) return null; // champ vide = pas d'erreur (car pas obligatoire)

  const errors: any = {};

  if (value.length < 8) {
    errors.minLength = true;
  }
  if (!/[a-z]/.test(value)) {
    errors.lowercase = true;
  }
  if (!/[A-Z]/.test(value)) {
    errors.uppercase = true;
  }
  if (!/[0-9]/.test(value)) {
    errors.digit = true;
  }
  if (!/[^a-zA-Z0-9 ]/.test(value)) { // ton test pour caractère spécial
    errors.special = true;
  }

  return Object.keys(errors).length ? errors : null;
}
  onSubmit() {
    if (!this.atLeastOneFieldFilled) {
      this.snackbar.show("Veuillez remplir au moins un champ.", 5000, 'bottom');
      return;
    }

    const { username, email, password } = this.profileForm.value;
    this.userServ.updateUser(email, username, password).subscribe({
      next: () => {
        this.snackbar.show("Profil mis à jour !", 5000, 'bottom');
        this.profileForm.reset();
      },
      error: (err) => {
        console.error('Erreur lors de la mise à jour du profil', err);
        if(err.error.message)
        {
          this.snackbar.show(err.error.message, 5000, 'bottom');
        }
        else{
          this.snackbar.show("Erreur lors de la mise à jour du profil", 5000, 'bottom');
        }
      }
    });
  }
  unsubscribe(theme:Theme){
    this.themeServ.subscribeOrUnsubscribe(theme.idTheme).subscribe({
      next: () => {
        this.abonnements = this.abonnements.filter(id => id !== theme.idTheme);
        this.snackbar.show(`Désabonné du thème ${theme.name}`,5000,'bottom');
      },
      error: (err) => {
        console.error('Erreur lors du désabonnement', err);
        this.snackbar.show("Erreur lors du désabonnement",5000,'bottom');
      }
    });
  }

}
