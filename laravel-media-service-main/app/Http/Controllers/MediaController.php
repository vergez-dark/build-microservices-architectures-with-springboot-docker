<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Media;
use Illuminate\Support\Facades\Storage;

class MediaController extends Controller
{
    // Get all media by post id
    public function getMediaByPostId($post_id)
    {
        $media = Media::where('post_id', $post_id)->get();
        if ($media->isEmpty()) {
            return response()->json([], 200);
        }

        return response()->json($media, 200);
    }

    // Get all media
    public function getAllMedia()
    {
        $media = Media::all();

        return response()->json($media, 200);
    }

    // Get media by id
    public function getMediaById($id)
    {
        $media = Media::find($id);
        if (!$media) {
            return response()->json(null, 200);
        }

        return response()->json($media, 200);
    }

    // Create media - upload file and save to database
    public function createMedia(Request $request)
    {
        $validated = $request->validate([
           'url_media' => ['required', 'image','mimes:jpg,jpeg,png,gif,mp4,mov,avi', 'max:4096'],
           'post_id' => ['required', 'numeric'],
        ]);
        $file = $request->file('url_media');
        $path = $file->store('medias', 'public');
        $url = "https://2t1tj96z-8000.use.devtunnels.ms/storage/{$path}";
        $media = Media::create([
            'name' => $file->getClientOriginalName(),
            'url_media' => $url,
            'post_id' => $request->post_id
        ]);

        return response()->json($media, 201);
    }

    // Update media
    public function updateMedia($id, Request $request)
    {
        $media = Media::find($id);
        if (!$media) {
            return response()->json(null, 200);
        }

        $validated = $request->validate([
            'url_media' => ['required', 'image','mimes:jpg,jpeg,png,gif,mp4,mov,avi', 'max:4096'],
            'post_id' => ['required', 'numeric'],
        ]);

        // Suppression de l'ancien fichier
        Storage::disk('public')->delete($media->url_media);

        $file = $request->file('url_media');
        $path = $file->store('medias', 'public');

        $media->update([
            'name' => $file->getClientOriginalName(),
            'url_media' => $path,
            'post_id' => $request->post_id
        ]);

        return response()->json($media, 200);
    }

    // Delete media
    public function deleteMedia($id)
    {
        $media = Media::find($id);
        if (!$media) {
            return response()->json(null, 200);
        }

        // Suppression du fichier associé
        Storage::disk('public')->delete($media->url_media);
        $media->delete();

        return response()->json(['message' => 'Media deleted successfully'], 200);
    }

    // Delete all media by post id
    public function deleteAllMediaByPostId($post_id)
    {
        $media = Media::where('post_id', $post_id)->get();
        if ($media->isEmpty()) {
            return response()->json(null, 200);
        }

        // Suppression de tous les fichiers associés
        foreach ($media as $item) {
            Storage::disk('public')->delete($item->url_media);
        }

        Media::where('post_id', $post_id)->delete();

        return response()->json(['message' => 'All media deleted successfully'], 200);
    }
}
