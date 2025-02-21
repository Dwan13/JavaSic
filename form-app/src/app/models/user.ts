
export class User {

    id: number = 0;
    name!: string;
    lastname!: string;
    email!: string;
    username!: string;
    password!: string;
    locked_account?: boolean = false;
    failed_attempts?: number = 0;
}