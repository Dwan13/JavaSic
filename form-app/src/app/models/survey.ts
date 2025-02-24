export class Survey {
    id: number = 0;
    email!: string;
    document_number!: number;
    comments!: string;
    response_date!: string | null; // Permitir null
    brand_id!: number;
    user_id!:number;
    content?: any;
    brand!: { id: number; name?: string | null } | null; 

}
