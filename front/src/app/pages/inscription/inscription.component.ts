
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { AuthService } from '../../services/Auth/auth.service';
import { Router } from '@angular/router';



function passwordValidator(control: AbstractControl): ValidationErrors | null {
  const value: string = control.value ?? '';
  if (!value) return null; // champ vide = pas d'erreur (facultatif)

  const errors: any = {};

  if (value.length < 8) {
    // clé 'minlength' pour être cohérent avec Angular
    errors['minlength'] = { requiredLength: 8, actualLength: value.length };
  }
  if (!/[a-z]/.test(value)) {
    errors['lowercase'] = true;
  }
  if (!/[A-Z]/.test(value)) {
    errors['uppercase'] = true;
  }
  if (!/[0-9]/.test(value)) {
    errors['digit'] = true;
  }
  // ton test pour caractère spécial
  if (!/[^a-zA-Z0-9 ]/.test(value)) {
    errors['special'] = true;
  }

  return Object.keys(errors).length ? errors : null;
}
@Component({
  selector: 'app-inscription',
  templateUrl: './inscription.component.html',
  styleUrls: ['./inscription.component.scss'],
})
export class InscriptionComponent implements OnInit {
  registerError:string ="";
  userForm!: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.userForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required,
        passwordValidator
      ]]
    });
  }


  get f() {
    return this.userForm.controls;
  }

  onSubmit(): void {
    this.registerError="";
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }
    this.authService.register(this.userForm.value).subscribe({
      next: () => {
        this.router.navigate(['accueil']);
      },
      error: (err) => {
        console.log(err);
        if(err?.error.message){
          this.registerError = err?.error.message;
        }
        else{
          this.registerError=err.message;
        }
      }
    });
    
  }

}
