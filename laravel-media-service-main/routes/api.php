<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\MediaController;


//  media routes

Route::get('/media', [MediaController::class, 'getAllMedia'])->name('media.all');
Route::get('/media/{id}', [MediaController::class, 'getMediaById']);
Route::get('/media/post/{post_id}', [MediaController::class, 'getMediaByPostId'])->name('media.post');

Route::post('/media', [MediaController::class, 'createMedia'])->name('media.create');
Route::put('/media/{id}', [MediaController::class, 'updateMedia'])->name('media.update');
Route::delete('/media/{id}', [MediaController::class, 'deleteMedia'])->name('media.delete');

Route::delete('/media/post/{post_id}', [MediaController::class, 'deleteAllMediaByPostId'])->name('media.delete.post');

