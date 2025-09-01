import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/Auth/auth.service';

@Component({
  selector: 'app-connexion',
  templateUrl: './connexion.component.html',
  styleUrls: ['./connexion.component.scss'],
})
export class ConnexionComponent implements OnInit {

  loginForm: FormGroup;
  loginError: string  = "";

  constructor(private fb: FormBuilder, private authService: AuthService, private router:Router) {
    this.loginForm = this.fb.group({
      identifier: ['', Validators.required],
      password: ['', Validators.required]
    });
  }
  ngOnInit(): void {
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const { identifier, password } = this.loginForm.value;

      this.authService.login(this.loginForm.value).subscribe({
      next: () => {
        this.router.navigate(['accueil']);
      },
      error: (err) => {
        console.log(err);
        if(err?.error.message){
          this.loginError = err?.error.message;
        }
        else{
          this.loginError=err.message;
        }
      }
    });
    }
  }
}
