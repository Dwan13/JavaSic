import { createReducer, on } from '@ngrx/store';
import { setUser, logout } from './auth.actions';

export interface AuthState {
  userId: string | null;
  isAuth: boolean;
}

export const initialState: AuthState = {
  userId: null,
  isAuth: false,
};

export const authReducer = createReducer(
  initialState,
  on(setUser, (state, { userId }) => ({ ...state, userId, isAuth: true })),
  on(logout, () => initialState)
);
