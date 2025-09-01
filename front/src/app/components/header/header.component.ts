import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/Auth/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  private _isLogged:boolean=false;
  isMenuOpen = false;
  constructor(private router:Router, private auth:AuthService) { }

  ngOnInit(): void {
    this._isLogged=!(this.router.url == "/connexion" || this.router.url=="/inscription");
  }
  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  closeMenu() {
    this.isMenuOpen = false;
  }
  get isLogged(){
    return this._isLogged;
  }
  logout(){
    this.auth.logout();
    this.router.navigateByUrl('');
  }
}
